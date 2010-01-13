/*
 * GWT-Ext Widget Library
 * Copyright 2007 - 2008, GWT-Ext LLC., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
 
package com.bkitmobile.poma.ui.client.imagechooser;

/**
 * Describe each icon image data, reference from GWTExt example
 * @author Tam Vo Minh
 *
 */
public class ImageData {
    private String name = null;
    private String url = null;
    private String fileName = null;
    private long size = 0;
    private String searchString = null;
    private String foundLocation = null;
    private String keyword[] = null;

    public String getName() {
        return name;
    }

    /**
     * Clear all properties
     */
    public void clear() {
        name = null;
        url = null;
        fileName = null;
        size = 0;
        searchString = null;
        foundLocation = null;
        keyword = null;
    }

    /**
     * Set name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get URL
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set URL
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get File Name
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set File Name
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get Size
     * @return
     */
    public long getSize() {
        return size;
    }

    /**
     * Set size
     * @param size
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Get search string
     * @return
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * Set search string for each image
     * @param searchString
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getFoundLocation() {
        return foundLocation;
    }

    public void setFoundLocation(String foundLocation) {
        this.foundLocation = foundLocation;
    }

    public String[] getKeyword() {
        return keyword;
    }

    public void setKeyword(String[] keyword) {
        this.keyword = keyword;
    }
}
