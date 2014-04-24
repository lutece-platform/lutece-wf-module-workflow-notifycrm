/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.plugins.workflow.modules.notifycrm.util.annotation.NotifyCRMConfig;
import fr.paris.lutece.plugins.workflownotify.business.TaskNotifyConfig;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 *
 * TaskNotifyCRMConfig
 *
 */
@NotifyCRMConfig
public class TaskNotifyCRMConfig extends TaskNotifyConfig
{
    @NotNull
    @Min( 1 )
    private int _nIdDirectory;
    @NotNull
    @Min( 1 )
    private int _nPositionEntryDirectoryIdDemand;
    private int _nPositionEntryDirectoryUserGuid;
    private int _nPositionEntryDirectoryCrmWebAppCode;
     
    private boolean _bSendNotification;
    @NotNull
    private String _strStatusText;
  

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
   
	/**
	 * 
	 * @return  the position Entry directory crm webapp code
	 */
	public int getPositionEntryDirectoryCrmWebAppCode() {
		return _nPositionEntryDirectoryCrmWebAppCode;
	}
	
	/**
	 * Set position Entry directory crm webapp code
	 * @param positionEntryCrmWebAppCode Set position Entry directory crm webapp code
	 */
	public void setPositionEntryDirectoryCrmWebAppCode(int nPositionEntryCrmWebAppCode) {
		this._nPositionEntryDirectoryCrmWebAppCode = nPositionEntryCrmWebAppCode;
	}
}
