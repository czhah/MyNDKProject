package com.thedream.cz.myndkproject.data.entity;

import com.thedream.cz.myndkproject.listener.ICommand;

/**
 * Created by cz on 2017/12/23.
 */

public class RightCommand implements ICommand {

    private TetrisMachine tetrisMachine;

    public RightCommand(TetrisMachine tetrisMachine) {
        this.tetrisMachine = tetrisMachine;
    }

    @Override
    public void execute() {
        if (tetrisMachine != null) tetrisMachine.toRight();
    }
}
