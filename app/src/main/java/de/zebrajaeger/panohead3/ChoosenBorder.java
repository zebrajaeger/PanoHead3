package de.zebrajaeger.panohead3;

/**
 * Created by Lars Brandt on 28.07.2017.
 */

public class ChoosenBorder {
    private boolean top = true;
    private boolean bot = false;
    private boolean left = true;
    private boolean right = false;

    public ChoosenBorder() {
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
