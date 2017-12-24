package com.thedream.cz.myndkproject.data.entity;

import com.thedream.cz.myndkproject.listener.ICommand;

/**
 * Created by cz on 2017/12/23.
 */

public class LeftCommand implements ICommand {

    private TetrisMachine machine;

    public LeftCommand(TetrisMachine machine) {
        this.machine = machine;
    }

    @Override
    public void execute() {
        if (machine != null) machine.toLeft();
    }
}
