package de.zebrajaeger.grblconnector.grbl;

import de.zebrajaeger.grblconnector.grbl.command.Command;
import de.zebrajaeger.grblconnector.grbl.command.CommandList;

public class GrblEx extends Grbl implements Grbl.Listener {
    private Object cmdLock = new Object();

    private String lastAnswer;
    private int timeout;
    private boolean waiting = false;

    public GrblEx(int timeout) {
        this.timeout = timeout;
        setListener(this);
    }

    public void execute(CommandList list) throws InterruptedException {
        for (Command c : list.getCommands()) {
            c.setAnswer(execute(c.getCommand()));
        }
    }

    public void execute(Command command) throws InterruptedException {
        command.setAnswer(execute(command.getCommand()));
    }

    public String execute(String cmd) throws InterruptedException {
        synchronized (cmdLock) {
            while (waiting) {
                cmdLock.wait();
            }

            waiting = true;
            String result = null;
            if (addCommand(cmd)) {
                cmdLock.wait(timeout);
                result = lastAnswer;
            }

            waiting = false;
            cmdLock.notifyAll();
            return result;
        }
    }

    @Override
    public void onGrblResult(String result) {
        synchronized (cmdLock) {
            if (waiting) {
                lastAnswer = result;
                cmdLock.notifyAll();
            }
        }
    }
}
