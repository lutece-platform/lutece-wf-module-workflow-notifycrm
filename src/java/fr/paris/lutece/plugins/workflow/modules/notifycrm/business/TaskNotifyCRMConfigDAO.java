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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * TaskNotifyCRMConfigDAO
 *
 */
public class TaskNotifyCRMConfigDAO implements ITaskNotifyCRMConfigDAO
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = " SELECT id_task, id_directory, position_directory_entry_id_demand, sender_name, subject, message, status_text " +
        " FROM task_notify_crm_cf  WHERE id_task = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO task_notify_crm_cf( id_task, id_directory, position_directory_entry_id_demand, sender_name, subject, message, status_text )" +
        " VALUES ( ?,?,?,?,?,?,? ) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE task_notify_crm_cf SET id_directory = ?, position_directory_entry_id_demand = ?, sender_name = ?, subject = ?, message = ?, status_text = ? " +
        " WHERE id_task = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM task_notify_crm_cf WHERE id_task = ? ";
    private static final String SQL_QUERY_FIND_ALL = " SELECT id_task, id_directory, position_directory_entry_id_demand, sender_name, subject, message, status_text " +
        " FROM task_notify_crm_cf ";

    /**
     * {@inheritDoc}
     */
    public synchronized void insert( TaskNotifyCRMConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, config.getIdTask(  ) );
        daoUtil.setInt( nIndex++, config.getIdDirectory(  ) );
        daoUtil.setInt( nIndex++, config.getPositionEntryDirectoryIdDemand(  ) );
        daoUtil.setString( nIndex++, config.getSenderName(  ) );
        daoUtil.setString( nIndex++, config.getSubject(  ) );
        daoUtil.setString( nIndex++, config.getMessage(  ) );
        daoUtil.setString( nIndex++, config.getStatusText(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void store( TaskNotifyCRMConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, config.getIdDirectory(  ) );
        daoUtil.setInt( nIndex++, config.getPositionEntryDirectoryIdDemand(  ) );
        daoUtil.setString( nIndex++, config.getSenderName(  ) );
        daoUtil.setString( nIndex++, config.getSubject(  ) );
        daoUtil.setString( nIndex++, config.getMessage(  ) );
        daoUtil.setString( nIndex++, config.getStatusText(  ) );

        daoUtil.setInt( nIndex++, config.getIdTask(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public TaskNotifyCRMConfig load( int nIdTask, Plugin plugin )
    {
        TaskNotifyCRMConfig config = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );

        daoUtil.setInt( 1, nIdTask );

        daoUtil.executeQuery(  );

        int nIndex = 1;

        if ( daoUtil.next(  ) )
        {
            config = new TaskNotifyCRMConfig(  );
            config.setIdTask( daoUtil.getInt( nIndex++ ) );
            config.setIdDirectory( daoUtil.getInt( nIndex++ ) );
            config.setPositionEntryDirectoryIdDemand( daoUtil.getInt( nIndex++ ) );
            config.setSenderName( daoUtil.getString( nIndex++ ) );
            config.setSubject( daoUtil.getString( nIndex++ ) );
            config.setMessage( daoUtil.getString( nIndex++ ) );
            config.setStatusText( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free(  );

        return config;
    }

    /**
     * {@inheritDoc}
     */
    public void delete( int nIdTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );

        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public List<TaskNotifyCRMConfig> loadAll( Plugin plugin )
    {
        List<TaskNotifyCRMConfig> configList = new ArrayList<TaskNotifyCRMConfig>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL, plugin );

        daoUtil.executeQuery(  );

        int nIndex = 1;

        if ( daoUtil.next(  ) )
        {
            TaskNotifyCRMConfig config = new TaskNotifyCRMConfig(  );
            config.setIdTask( daoUtil.getInt( nIndex++ ) );
            config.setIdDirectory( daoUtil.getInt( nIndex++ ) );
            config.setPositionEntryDirectoryIdDemand( daoUtil.getInt( nIndex++ ) );
            config.setSenderName( daoUtil.getString( nIndex++ ) );
            config.setSubject( daoUtil.getString( nIndex++ ) );
            config.setMessage( daoUtil.getString( nIndex++ ) );
            config.setStatusText( daoUtil.getString( nIndex++ ) );
            configList.add( config );
        }

        daoUtil.free(  );

        return configList;
    }
}
