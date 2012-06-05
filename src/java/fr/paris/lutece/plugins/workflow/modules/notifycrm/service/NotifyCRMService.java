/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.EntryTypeGeolocation;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.business.RecordFieldHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.business.TaskNotifyCRMConfig;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.util.constants.NotifyCRMConstants;
import fr.paris.lutece.plugins.workflow.service.security.IWorkflowUserAttributesManager;
import fr.paris.lutece.plugins.workflow.service.taskinfo.ITaskInfoProvider;
import fr.paris.lutece.plugins.workflow.service.taskinfo.TaskInfoManager;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.service.task.ITaskService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.ReferenceList;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * NotifyCRMService
 *
 */
public final class NotifyCRMService implements INotifyCRMService
{
    public static final String BEAN_SERVICE = "workflow-notifycrm.notifyCRMService";
    private List<Integer> _listAcceptedEntryTypesIdDemand;
    private List<Integer> _listRefusedEntryTypes;
    private List<Integer> _listAcceptedEntryTypesUserGuid;
    @Inject
    @Named( NotifyCRMConstants.BEAN_TASK_CONFIG_SERVICE )
    private ITaskConfigService _taskNotifyCRMConfigService;
    @Inject
    private IWorkflowUserAttributesManager _userAttributesManager;
    @Inject
    private ITaskService _taskService;

    /**
     * Private constructor
     */
    private NotifyCRMService(  )
    {
        // Init list accepted entry types for id demand
        _listAcceptedEntryTypesIdDemand = fillListEntryTypes( NotifyCRMConstants.PROPERTY_ACCEPTED_DIRECTORY_ENTRY_TYPE_ID_DEMAND );

        // Init list refused entry types
        _listRefusedEntryTypes = fillListEntryTypes( NotifyCRMConstants.PROPERTY_REFUSED_DIRECTORY_ENTRY_TYPE );

        // Init list accepted entry types for user guid
        _listAcceptedEntryTypesUserGuid = fillListEntryTypes( NotifyCRMConstants.PROPERTY_ACCEPTED_DIRECTORY_ENTRY_TYPE_USER_GUID );
    }

    // CHECKS

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEntryTypeRefused( int nIdEntryType )
    {
        boolean bIsRefused = true;

        if ( ( _listRefusedEntryTypes != null ) && !_listRefusedEntryTypes.isEmpty(  ) )
        {
            bIsRefused = _listRefusedEntryTypes.contains( nIdEntryType );
        }

        return bIsRefused;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEntryTypeIdDemandAccepted( int nIdEntryType )
    {
        boolean bIsAccepted = false;

        if ( ( _listAcceptedEntryTypesIdDemand != null ) && !_listAcceptedEntryTypesIdDemand.isEmpty(  ) )
        {
            bIsAccepted = _listAcceptedEntryTypesIdDemand.contains( nIdEntryType );
        }

        return bIsAccepted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEntryTypeUserGuidAccepted( int nIdEntryType )
    {
        boolean bIsAccepted = false;

        if ( ( _listAcceptedEntryTypesUserGuid != null ) && !_listAcceptedEntryTypesUserGuid.isEmpty(  ) )
        {
            bIsAccepted = _listAcceptedEntryTypesUserGuid.contains( nIdEntryType );
        }

        return bIsAccepted;
    }

    // GETS

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getListDirectories(  )
    {
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        ReferenceList listDirectories = DirectoryHome.getDirectoryList( pluginDirectory );
        ReferenceList refenreceListDirectories = new ReferenceList(  );
        refenreceListDirectories.addItem( DirectoryUtils.CONSTANT_ID_NULL, StringUtils.EMPTY );

        if ( listDirectories != null )
        {
            refenreceListDirectories.addAll( listDirectories );
        }

        return refenreceListDirectories;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IEntry> getListEntries( int nIdTask )
    {
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        TaskNotifyCRMConfig config = _taskNotifyCRMConfigService.findByPrimaryKey( nIdTask );

        List<IEntry> listEntries = new ArrayList<IEntry>(  );

        if ( config != null )
        {
            EntryFilter entryFilter = new EntryFilter(  );
            entryFilter.setIdDirectory( config.getIdDirectory(  ) );

            listEntries = EntryHome.getEntryList( entryFilter, pluginDirectory );
        }

        return listEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getListEntriesIdDemand( int nIdTask, Locale locale )
    {
        ReferenceList refenreceListEntries = new ReferenceList(  );
        refenreceListEntries.addItem( DirectoryUtils.CONSTANT_ID_NULL, DirectoryUtils.EMPTY_STRING );

        for ( IEntry entry : getListEntries( nIdTask ) )
        {
            int nIdEntryType = entry.getEntryType(  ).getIdType(  );

            if ( isEntryTypeIdDemandAccepted( nIdEntryType ) )
            {
                refenreceListEntries.addItem( entry.getPosition(  ), buildReferenceEntryToString( entry, locale ) );
            }
        }

        return refenreceListEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getListEntriesUserGuid( int nIdTask, Locale locale )
    {
        ReferenceList refenreceListEntries = new ReferenceList(  );
        refenreceListEntries.addItem( DirectoryUtils.CONSTANT_ID_NULL, DirectoryUtils.EMPTY_STRING );

        for ( IEntry entry : getListEntries( nIdTask ) )
        {
            int nIdEntryType = entry.getEntryType(  ).getIdType(  );

            if ( isEntryTypeUserGuidAccepted( nIdEntryType ) )
            {
                refenreceListEntries.addItem( entry.getPosition(  ), buildReferenceEntryToString( entry, locale ) );
            }
        }

        return refenreceListEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IEntry> getListEntriesFreemarker( int nIdTask )
    {
        List<IEntry> listEntries = new ArrayList<IEntry>(  );

        for ( IEntry entry : getListEntries( nIdTask ) )
        {
            int nIdEntryType = entry.getEntryType(  ).getIdType(  );

            if ( !isEntryTypeRefused( nIdEntryType ) )
            {
                listEntries.add( entry );
            }
        }

        return listEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdDemand( TaskNotifyCRMConfig config, int nIdRecord, int nIdDirectory )
    {
        String strIdDemand = StringUtils.EMPTY;

        if ( config.getPositionEntryDirectoryIdDemand(  ) != DirectoryUtils.CONSTANT_ID_NULL )
        {
            strIdDemand = getRecordFieldValue( config.getPositionEntryDirectoryIdDemand(  ), nIdRecord, nIdDirectory );
        }

        return strIdDemand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserGuid( TaskNotifyCRMConfig config, int nIdRecord, int nIdDirectory )
    {
        String strUserGuid = StringUtils.EMPTY;

        if ( config.getPositionEntryDirectoryUserGuid(  ) != DirectoryUtils.CONSTANT_ID_NULL )
        {
            strUserGuid = getRecordFieldValue( config.getPositionEntryDirectoryUserGuid(  ), nIdRecord, nIdDirectory );
        }

        return strUserGuid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ITask> getListTasks( int nIdAction, Locale locale )
    {
        List<ITask> listTasks = new ArrayList<ITask>(  );

        for ( ITask task : _taskService.getListTaskByIdAction( nIdAction, locale ) )
        {
            for ( ITaskInfoProvider provider : TaskInfoManager.getProvidersList(  ) )
            {
                if ( task.getTaskType(  ).getKey(  ).equals( provider.getTaskType(  ).getKey(  ) ) )
                {
                    listTasks.add( task );

                    break;
                }
            }
        }

        return listTasks;
    }

    // OTHERS

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> fillModel( TaskNotifyCRMConfig config, Record record, Directory directory,
        HttpServletRequest request, int nIdAction, int nIdHistory )
    {
        Locale locale = getLocale( request );
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( NotifyCRMConstants.MARK_MESSAGE, config.getMessage(  ) );
        model.put( NotifyCRMConstants.MARK_DIRECTORY_TITLE, directory.getTitle(  ) );
        model.put( NotifyCRMConstants.MARK_DIRECTORY_DESCRIPTION, directory.getDescription(  ) );

        RecordFieldFilter recordFieldFilter = new RecordFieldFilter(  );
        recordFieldFilter.setIdRecord( record.getIdRecord(  ) );

        List<RecordField> listRecordField = RecordFieldHome.getRecordFieldList( recordFieldFilter, pluginDirectory );

        for ( RecordField recordField : listRecordField )
        {
            String strNewValue = StringUtils.EMPTY;

            if ( isEntryTypeRefused( recordField.getEntry(  ).getEntryType(  ).getIdType(  ) ) )
            {
                continue;
            }
            else if ( recordField.getEntry(  ) instanceof fr.paris.lutece.plugins.directory.business.EntryTypeGeolocation &&
                    !recordField.getField(  ).getTitle(  ).equals( EntryTypeGeolocation.CONSTANT_ADDRESS ) )
            {
                continue;
            }
            else if ( ( recordField.getField(  ) != null ) && ( recordField.getField(  ).getTitle(  ) != null ) &&
                    !( recordField.getEntry(  ) instanceof fr.paris.lutece.plugins.directory.business.EntryTypeGeolocation ) )
            {
                strNewValue = recordField.getField(  ).getTitle(  );
            }
            else if ( recordField.getEntry(  ) instanceof fr.paris.lutece.plugins.directory.business.EntryTypeFile &&
                    ( recordField.getFile(  ) != null ) && ( recordField.getFile(  ).getTitle(  ) != null ) )
            {
                strNewValue = recordField.getFile(  ).getTitle(  );
            }
            else
            {
                strNewValue = recordField.getEntry(  ).convertRecordFieldValueToString( recordField, locale, false,
                        false );
            }

            recordField.setEntry( EntryHome.findByPrimaryKey( recordField.getEntry(  ).getIdEntry(  ), pluginDirectory ) );

            String strKey = NotifyCRMConstants.MARK_POSITION + recordField.getEntry(  ).getPosition(  );
            String strOldValue = ( (String) model.get( strKey ) );

            if ( StringUtils.isNotBlank( strOldValue ) && StringUtils.isNotBlank( strNewValue ) )
            {
                // Add markers for message
                model.put( strKey, strNewValue + NotifyCRMConstants.COMMA + strOldValue );
            }
            else if ( strNewValue != null )
            {
                model.put( strKey, strNewValue );
            }
            else
            {
                model.put( strKey, StringUtils.EMPTY );
            }
        }

        if ( ( directory.getIdWorkflow(  ) != DirectoryUtils.CONSTANT_ID_NULL ) &&
                WorkflowService.getInstance(  ).isAvailable(  ) )
        {
            State state = WorkflowService.getInstance(  )
                                         .getState( record.getIdRecord(  ), Record.WORKFLOW_RESOURCE_TYPE,
                    directory.getIdWorkflow(  ), null );
            model.put( NotifyCRMConstants.MARK_STATUS, state.getName(  ) );
        }

        // Fill the model with the user attributes
        String strUserGuid = getUserGuid( config, record.getIdRecord(  ), directory.getIdDirectory(  ) );
        fillModelWithUserAttributes( model, strUserGuid );

        // Fill the model with the info of other tasks
        for ( ITask task : getListTasks( nIdAction, locale ) )
        {
            model.put( NotifyCRMConstants.MARK_TASK + task.getId(  ),
                TaskInfoManager.getTaskResourceInfo( nIdHistory, task.getId(  ), request ) );
        }

        return model;
    }

    // PRIVATES METHODS

    /**
     * Get the record field value
     * @param nPosition the position of the entry
     * @param nIdRecord the id record
     * @param nIdDirectory the id directory
     * @return the record field value
     */
    private String getRecordFieldValue( int nPosition, int nIdRecord, int nIdDirectory )
    {
        String strRecordFieldValue = StringUtils.EMPTY;
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        // RecordField
        EntryFilter entryFilter = new EntryFilter(  );
        entryFilter.setPosition( nPosition );
        entryFilter.setIdDirectory( nIdDirectory );

        List<IEntry> listEntries = EntryHome.getEntryList( entryFilter, pluginDirectory );

        if ( ( listEntries != null ) && !listEntries.isEmpty(  ) )
        {
            IEntry entry = listEntries.get( 0 );
            RecordFieldFilter recordFieldFilterEmail = new RecordFieldFilter(  );
            recordFieldFilterEmail.setIdDirectory( nIdDirectory );
            recordFieldFilterEmail.setIdEntry( entry.getIdEntry(  ) );
            recordFieldFilterEmail.setIdRecord( nIdRecord );

            List<RecordField> listRecordFields = RecordFieldHome.getRecordFieldList( recordFieldFilterEmail,
                    pluginDirectory );

            if ( ( listRecordFields != null ) && !listRecordFields.isEmpty(  ) && ( listRecordFields.get( 0 ) != null ) )
            {
                RecordField recordFieldIdDemand = listRecordFields.get( 0 );
                strRecordFieldValue = recordFieldIdDemand.getValue(  );

                if ( recordFieldIdDemand.getField(  ) != null )
                {
                    strRecordFieldValue = recordFieldIdDemand.getField(  ).getTitle(  );
                }
            }
        }

        return strRecordFieldValue;
    }

    /**
     * Get the locale
     * @param request the HTTP request
     * @return the locale
     */
    private Locale getLocale( HttpServletRequest request )
    {
        Locale locale = null;

        if ( request != null )
        {
            locale = request.getLocale(  );
        }
        else
        {
            locale = I18nService.getDefaultLocale(  );
        }

        return locale;
    }

    /**
     * Fills the model with user attributes
     * @param model the model
     * @param strUserGuid the user guid
     */
    private void fillModelWithUserAttributes( Map<String, Object> model, String strUserGuid )
    {
        if ( _userAttributesManager.isEnabled(  ) && StringUtils.isNotBlank( strUserGuid ) )
        {
            Map<String, String> mapUserAttributes = _userAttributesManager.getAttributes( strUserGuid );
            String strFirstName = mapUserAttributes.get( LuteceUser.NAME_GIVEN );
            String strLastName = mapUserAttributes.get( LuteceUser.NAME_FAMILY );
            String strEmail = mapUserAttributes.get( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL );
            String strPhoneNumber = mapUserAttributes.get( LuteceUser.BUSINESS_INFO_TELECOM_TELEPHONE_NUMBER );

            model.put( NotifyCRMConstants.MARK_FIRST_NAME,
                StringUtils.isNotEmpty( strFirstName ) ? strFirstName : StringUtils.EMPTY );
            model.put( NotifyCRMConstants.MARK_LAST_NAME,
                StringUtils.isNotEmpty( strLastName ) ? strLastName : StringUtils.EMPTY );
            model.put( NotifyCRMConstants.MARK_EMAIL, StringUtils.isNotEmpty( strEmail ) ? strEmail : StringUtils.EMPTY );
            model.put( NotifyCRMConstants.MARK_PHONE_NUMBER,
                StringUtils.isNotEmpty( strPhoneNumber ) ? strPhoneNumber : StringUtils.EMPTY );
        }
    }

    /**
     * Build the reference entry into String
     * @param entry the entry
     * @param locale the Locale
     * @return the reference entry
     */
    private String buildReferenceEntryToString( IEntry entry, Locale locale )
    {
        StringBuilder sbReferenceEntry = new StringBuilder(  );
        sbReferenceEntry.append( entry.getPosition(  ) );
        sbReferenceEntry.append( NotifyCRMConstants.SPACE + NotifyCRMConstants.OPEN_BRACKET );
        sbReferenceEntry.append( entry.getTitle(  ) );
        sbReferenceEntry.append( NotifyCRMConstants.SPACE + NotifyCRMConstants.HYPHEN + NotifyCRMConstants.SPACE );
        sbReferenceEntry.append( I18nService.getLocalizedString( entry.getEntryType(  ).getTitleI18nKey(  ), locale ) );
        sbReferenceEntry.append( NotifyCRMConstants.CLOSED_BRACKET );

        return sbReferenceEntry.toString(  );
    }

    /**
     * Fill the list of entry types
     * @param strPropertyEntryTypes the property containing the entry types
     * @return a list of integer
     */
    private static List<Integer> fillListEntryTypes( String strPropertyEntryTypes )
    {
        List<Integer> listEntryTypes = new ArrayList<Integer>(  );
        String strEntryTypes = AppPropertiesService.getProperty( strPropertyEntryTypes );

        if ( StringUtils.isNotBlank( strEntryTypes ) )
        {
            String[] listAcceptEntryTypesForIdDemand = strEntryTypes.split( NotifyCRMConstants.COMMA );

            for ( String strAcceptEntryType : listAcceptEntryTypesForIdDemand )
            {
                if ( StringUtils.isNotBlank( strAcceptEntryType ) && StringUtils.isNumeric( strAcceptEntryType ) )
                {
                    int nAcceptedEntryType = Integer.parseInt( strAcceptEntryType );
                    listEntryTypes.add( nAcceptedEntryType );
                }
            }
        }

        return listEntryTypes;
    }
}
