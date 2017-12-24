package com.thedream.cz.myndkproject.data.entity;

/**
 * Created by cz on 2017/12/23.
 */

public class Buttons {

    private LeftCommand leftCommand;
    private RightCommand rightCommand;

    /**
     * 设置向左命令
     *
     * @param leftCommand
     */
    public void setLeftCommand(LeftCommand leftCommand) {
        this.leftCommand = leftCommand;
    }

    /**
     * 设置向右命令
     *
     * @param rightCommand
     */
    public void setRightCommand(RightCommand rightCommand) {
        this.rightCommand = rightCommand;
    }

    public void toLeft() {
        leftCommand.execute();
    }


    public void toRight() {
        rightCommand.execute();
    }

}
