package de.zebrajaeger.grblconnector.grbl.command;

import de.zebrajaeger.grblconnector.grbl.move.Pos;

public class Commands {
    public static CommandList getInitCommands() {
        return new CommandList(new String[]{"$X\n"});
    }

    public static CommandList getJogCancelCommands() {
        return new CommandList(new String[]{"!~\n"});
    }

    public static CommandList createMoveToCommand(Pos pos, float feedrate) {
        return new CommandList(new String[]{"G01X" + pos.getX() + "Y" + pos.getY() + "F" + feedrate + "\n"});
    }

    public static CommandList createCamTriggerCommand(boolean focus, boolean trigger) {
        // M3 = spindle CW: set spindle = HIGH & direction pin = LOW
        // M4 = spindle CCW: set spindle = HIGH & direction pin = HIGH
        // M5 = spindle OFF: set spindle = LOW & direction pin = LOW
        String[] cmds;
        if (!focus && !trigger) {
            cmds = new String[]{"M3\n", "M5\n"};
        } else if (focus && !trigger) {
            cmds = new String[]{"M3\n"};
        } else {
            cmds = new String[]{"M4\n"};
        }

        return new CommandList(cmds);
    }
}
