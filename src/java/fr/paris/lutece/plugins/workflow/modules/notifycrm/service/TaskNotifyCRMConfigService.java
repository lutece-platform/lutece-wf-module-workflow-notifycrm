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
package fr.paris.lutece.plugins.workflow.modules.notifycrm.service;

import fr.paris.lutece.plugins.workflow.modules.notifycrm.business.TaskNotifyCRMConfig;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.business.TaskNotifyCRMConfigHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 * TaskNotifyCRMConfigService
 *
 */
public final class TaskNotifyCRMConfigService
{
    private static final String BEAN_TASK_NOTIFY_CRM_CONFIG_SERVICE = "workflow-notifycrm.taskNotifyCRMConfigService";

    /**
     * Private constructor
     */
    private TaskNotifyCRMConfigService(  )
    {
    }

    /**
     * Get the instance of {@link TaskNotifyCRMConfigService}
     * @return the instance of {@link TaskNotifyCRMConfigService}
     */
    public static TaskNotifyCRMConfigService getService(  )
    {
        return (TaskNotifyCRMConfigService) SpringContextService.getPluginBean( NotifyCRMPlugin.PLUGIN_NAME,
            BEAN_TASK_NOTIFY_CRM_CONFIG_SERVICE );
    }

    /**
     * Create a new config
     * @param config the config
     */
    public void create( TaskNotifyCRMConfig config )
    {
        if ( config != null )
        {
            TaskNotifyCRMConfigHome.create( config );
        }
    }

    /**
     * Update a config
     * @param config the config
     */
    public void update( TaskNotifyCRMConfig config )
    {
        if ( config != null )
        {
            TaskNotifyCRMConfigHome.update( config );
        }
    }

    /**
     * Remove a config
     * @param nIdTask the task id
     */
    public void remove( int nIdTask )
    {
        TaskNotifyCRMConfigHome.remove( nIdTask );
    }

    /**
     * Find a config
     * @param nIdTask the id task
     * @return an instance of {@link TaskNotifyCRMConfig}
     */
    public TaskNotifyCRMConfig findByPrimaryKey( int nIdTask )
    {
        return TaskNotifyCRMConfigHome.findByPrimaryKey( nIdTask );
    }

    /**
     * Get all configs
     * @return a list of {@link TaskNotifyCRMConfig}
     */
    public List<TaskNotifyCRMConfig> findAll(  )
    {
        return TaskNotifyCRMConfigHome.findAll(  );
    }
}
