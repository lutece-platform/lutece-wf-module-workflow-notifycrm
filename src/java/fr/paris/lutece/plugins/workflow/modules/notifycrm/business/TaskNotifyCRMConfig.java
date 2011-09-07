/*
 * Copyright (c) 2002-2011, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.notifycrm.business;


/**
 *
 * TaskNotifyCRMConfig
 *
 */
public class TaskNotifyCRMConfig
{
    private int _nIdTask;
    private int _nIdDirectory;
    private int _nPositionEntryDirectoryIdDemand;
    private int _nPositionEntryDirectoryUserGuid;
    private boolean _bSendNotification;
    private String _strSubject;
    private String _strMessage;
    private String _strSenderName;
    private String _strStatusText;

    /**
     * Get the ID task
     * @return id Task
     */
    public int getIdTask(  )
    {
        return _nIdTask;
    }

    /**
     * Set id Task
     * @param idTask id task
     */
    public void setIdTask( int idTask )
    {
        _nIdTask = idTask;
    }

    /**
     * Get the ID directory
     * @return id directory
     */
    public int getIdDirectory(  )
    {
        return _nIdDirectory;
    }

    /**
     * Set id directory
     * @param idDirectory id directory
     */
    public void setIdDirectory( int idDirectory )
    {
        _nIdDirectory = idDirectory;
    }

    /**
     * Get the position of the entry directory id demand
     * @return position Entry directory ID demand
     */
    public int getPositionEntryDirectoryIdDemand(  )
    {
        return _nPositionEntryDirectoryIdDemand;
    }

    /**
     * Set position Entry directory id demand
     * @param nPositionEntryDirectoryIdDemand position of Entry directory id demand
     */
    public void setPositionEntryDirectoryIdDemand( int nPositionEntryDirectoryIdDemand )
    {
        _nPositionEntryDirectoryIdDemand = nPositionEntryDirectoryIdDemand;
    }

    /**
     * Get the position of the entry directory associated to the user guid
     * @return position Entry directory user guid
     */
    public int getPositionEntryDirectoryUserGuid(  )
    {
        return _nPositionEntryDirectoryUserGuid;
    }

    /**
     * Set position Entry directory user guid
     * @param nPositionEntryDirectoryUserGuid position of Entry directory user guid
     */
    public void setPositionEntryDirectoryUserGuid( int nPositionEntryDirectoryUserGuid )
    {
        _nPositionEntryDirectoryUserGuid = nPositionEntryDirectoryUserGuid;
    }

    /**
     * Set true if it must send a notification, false otherwise
     * @param bSendNotification true if it must send a notification, false otherwise
     */
    public void setSendNotification( boolean bSendNotification )
    {
        _bSendNotification = bSendNotification;
    }

    /**
     * Return true if it must send a notification, false otherwise
     * @return true if it must send a notification, false otherwise
     */
    public boolean getSendNotification(  )
    {
        return _bSendNotification;
    }

    /**
    * Get the subject
    * @return the subject of the message
    */
    public String getSubject(  )
    {
        return _strSubject;
    }

    /**
     * Set the subject of the message
     * @param subject the subject of the message
     */
    public void setSubject( String subject )
    {
        _strSubject = subject;
    }

    /**
     * Get the message
     * @return the message of the notification
     */
    public String getMessage(  )
    {
        return _strMessage;
    }

    /**
     * Set the message of the notification
     * @param message the message of the notification
     */
    public void setMessage( String message )
    {
        _strMessage = message;
    }

    /**
     * Get the sender name
     * @return the sender name
     */
    public String getSenderName(  )
    {
        return _strSenderName;
    }

    /**
     * Set the sender name
     * @param senderName  the sender name
     */
    public void setSenderName( String senderName )
    {
        _strSenderName = senderName;
    }

    /**
     * Set the status
     * @param strStatusText the status
     */
    public void setStatusText( String strStatusText )
    {
        _strStatusText = strStatusText;
    }

    /**
     * Get the status
     * @return the status
     */
    public String getStatusText(  )
    {
        return _strStatusText;
    }
}
