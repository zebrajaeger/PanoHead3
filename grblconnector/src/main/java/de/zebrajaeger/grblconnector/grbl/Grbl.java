package de.zebrajaeger.grblconnector.grbl;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class Grbl {
    private Listener listener;
    private ResultListener resultListener;
    private CommandSender commandSender;

    public void setListener(Listener listener) {
        this.listener = listener;
        if (resultListener != null) {
            resultListener.addListener(listener);
        }
    }

    public boolean addCommand(String cmd) {
        if (commandSender == null) {
            return false;
        }
        commandSender.addCommand(cmd);
        return true;
    }

    public void start(Streamable streamable) {
        if ((resultListener != null && resultListener.isAlive())
                || (commandSender != null && commandSender.isAlive())) {
            throw new IllegalStateException("at least one thread is running");
        } else {
            resultListener = new ResultListener(streamable);
            if (listener != null) {
                resultListener.addListener(listener);
            }
            commandSender = new CommandSender(streamable);

            resultListener.start();
            commandSender.start();
        }
    }

    public void stop() throws InterruptedException {
        if (resultListener != null) {
            resultListener.interrupt();
            resultListener.join();
        }

        if (commandSender != null) {
            commandSender.interrupt();
            commandSender.join();
        }
    }

    public enum GrblStatus {
        IDLE, RUN, HOLD, DOOR, HOME, ALARM, CHECK, UNKNOWN;

        public static GrblStatus of(String status) {
            status = status.toUpperCase();
            try {
                return GrblStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                Log.w("Could not interpret status '" + status + "'", e);
                return UNKNOWN;
            }
        }
    }

    public interface Listener {
        void onGrblResult(String result);
    }

    private class ResultListener extends Thread {

        private List<Listener> listener = new LinkedList<>();
        private Streamable streamable;

        public ResultListener(Streamable streamable) {
            this.streamable = streamable;
        }

        public void addListener(Listener listener) {
            this.listener.add(listener);
        }

        @Override
        public void run() {
            Log.i("ResultListener", "Thread started");
            StringBuilder sb = new StringBuilder(128);
            try {
                InputStream is = streamable.getInputStream();
                for (int received; (received = is.read()) >= 0; ) {
                    if (received == '\r' || received == '\n') {
                        if (listener != null && sb.length() > 0) {
                            Log.d("ResultListener", "Result received: '" + sb.toString() + "'");
                            String result = sb.toString();
                            for (Listener l : listener) {
                                l.onGrblResult(result);
                            }
                        }
                        sb = new StringBuilder(128);
                    } else {
                        sb.append((char) received);
                    }
                }
            } catch (IOException e) {
                Log.e("ResultListener", "Failed reading from stream", e);
            } finally {
                Log.i("ResultListener", "ResultListener: Thread stopped");
            }
        }
    }

    private class CommandSender extends Thread {

        private LinkedList<String> cmds = new LinkedList<>();
        private Streamable streamable;

        public CommandSender(Streamable streamable) {
            this.streamable = streamable;
        }

        public void addCommand(String cmd) {
            Log.d("CommandSender", "add new command: '" + cmd + "'");
            synchronized (this) {
                cmds.add(cmd);
                notify();
            }
            Log.d("CommandSender", "command added: '" + cmd + "'");
        }

        @Override
        public void run() {
            Log.i("CommandSender", "thread started");
            try {
                OutputStream os = streamable.getOutputStream();
                for (; ; ) {
                    synchronized (this) {
                        while (cmds.isEmpty()) {
                            wait();
                        }
                        String toSend = cmds.removeLast();
                        Log.d("CommandSender", "send new command: '" + toSend + "'");
                        os.write((toSend).getBytes());
                        Log.d("CommandSender", "command written: '" + toSend + "'");
                    }
                }
            } catch (InterruptedException e) {
                interrupt();
                Log.i("CommandSender", "Sender interrupted");
            } catch (IOException e) {
                Log.i("CommandSender", "could not send, e");
            } finally {
                Log.i("CommandSender", "thread stopped");
            }
        }
    }
}
