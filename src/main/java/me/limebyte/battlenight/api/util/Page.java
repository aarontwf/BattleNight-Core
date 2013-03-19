package me.limebyte.battlenight.api.util;

public interface Page {

    /**
     * Gets the page contents as an array of strings which represent lines.
     * 
     * @return the page lines
     */
    public String[] getPage();

    /**
     * Gets the width of this page in pixels.
     * 
     * @return the page width
     */
    public int getWidth();

    /**
     * Sets the width of this page in pixels.
     * 
     * @param width The width to set
     */
    public void setWidth(int width);

}
