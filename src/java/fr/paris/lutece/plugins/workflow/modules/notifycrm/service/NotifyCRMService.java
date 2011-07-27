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
import fr.paris.lutece.plugins.workflow.business.ActionHome;
import fr.paris.lutece.plugins.workflow.business.ResourceHistory;
import fr.paris.lutece.plugins.workflow.business.StateFilter;
import fr.paris.lutece.plugins.workflow.business.StateHome;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.business.TaskNotifyCRMConfig;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.util.constants.NotifyCRMConstants;
import fr.paris.lutece.plugins.workflow.service.WorkflowPlugin;
import fr.paris.lutece.portal.business.workflow.Action;
import fr.paris.lutece.portal.business.workflow.State;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.ReferenceList;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 *
 * NotifyCRMService
 *
 */
public final class NotifyCRMService
{
    private static final String BEAN_NOTIFY_CRM_SERVICE = "workflow-notifycrm.notifyCRMService";
    private List<Integer> _listRefusedEntryTypes;
    private List<Integer> _listAcceptedEntryTypes;

    /**
     * Private constructor
     */
    private NotifyCRMService(  )
    {
        // Init list accepted entry types
        _listAcceptedEntryTypes = new ArrayList<Integer>(  );

        String strAcceptEntryTypes = AppPropertiesService.getProperty( NotifyCRMConstants.PROPERTY_ACCEPTED_DIRECTORY_ENTRY_TYPE );
        String[] listAcceptEntryTypes = strAcceptEntryTypes.split( NotifyCRMConstants.COMMA );

        for ( String strAcceptEntryType : listAcceptEntryTypes )
        {
            if ( StringUtils.isNotBlank( strAcceptEntryType ) && StringUtils.isNumeric( strAcceptEntryType ) )
            {
                int nAcceptedEntryType = Integer.parseInt( strAcceptEntryType );
                _listAcceptedEntryTypes.add( nAcceptedEntryType );
            }
        }

        // Init list refused entry types
        _listRefusedEntryTypes = new ArrayList<Integer>(  );

        String strRefusedEntryTypes = AppPropertiesService.getProperty( NotifyCRMConstants.PROPERTY_REFUSED_DIRECTORY_ENTRY_TYPE );
        String[] listRefusedEntryTypes = strRefusedEntryTypes.split( NotifyCRMConstants.COMMA );

        for ( String strRefusedEntryType : listRefusedEntryTypes )
        {
            if ( StringUtils.isNotBlank( strRefusedEntryType ) && StringUtils.isNumeric( strRefusedEntryType ) )
            {
                int nRefusedEntryType = Integer.parseInt( strRefusedEntryType );
                _listRefusedEntryTypes.add( nRefusedEntryType );
            }
        }
    }

    /**
     * Get the instance of the service
     * @return the instance of the service
     */
    public static NotifyCRMService getService(  )
    {
        return (NotifyCRMService) SpringContextService.getPluginBean( NotifyCRMPlugin.PLUGIN_NAME,
            BEAN_NOTIFY_CRM_SERVICE );
    }

    /**
     * Check if the given entry type id is refused
     * @param nIdEntryType the id entry type
     * @return true if it is refused, false otherwise
     */
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
     * Check if the given entry type id is accepted
     * @param nIdEntryType the id entry type
     * @return true if it is accepted, false otherwise
     */
    public boolean isEntryTypeAccepted( int nIdEntryType )
    {
        boolean bIsAccepted = true;

        if ( ( _listAcceptedEntryTypes != null ) && !_listAcceptedEntryTypes.isEmpty(  ) )
        {
            bIsAccepted = _listAcceptedEntryTypes.contains( nIdEntryType );
        }

        return bIsAccepted;
    }

    /**
     * Get the list of states
     * @param nIdAction the id action
     * @return a ReferenceList
     */
    public ReferenceList getListStates( int nIdAction )
    {
        Plugin pluginWorkflow = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );
        ReferenceList referenceListStates = new ReferenceList(  );
        Action action = ActionHome.findByPrimaryKey( nIdAction, pluginWorkflow );

        if ( ( action != null ) && ( action.getWorkflow(  ) != null ) )
        {
            StateFilter stateFilter = new StateFilter(  );
            stateFilter.setIdWorkflow( action.getWorkflow(  ).getId(  ) );

            List<State> listStates = StateHome.getListStateByFilter( stateFilter, pluginWorkflow );

            referenceListStates.addItem( DirectoryUtils.CONSTANT_ID_NULL, StringUtils.EMPTY );
            referenceListStates.addAll( ReferenceList.convert( listStates, NotifyCRMConstants.ID,
                    NotifyCRMConstants.NAME, true ) );
        }

        return referenceListStates;
    }

    /**
     * Get the list of directorise
     * @return a ReferenceList
     */
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
     * Get the list of entries that have the accepted type (which are defined in <b>workflow-notifycrm.properties</b>)
     * @param nIdTask the id task
     * @param locale the Locale
     * @return a ReferenceList
     */
    public ReferenceList getListEntries( int nIdTask, Locale locale )
    {
        TaskNotifyCRMConfig config = TaskNotifyCRMConfigService.getService(  ).findByPrimaryKey( nIdTask );
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        ReferenceList refenreceListEntries = new ReferenceList(  );
        refenreceListEntries.addItem( DirectoryUtils.CONSTANT_ID_NULL, DirectoryUtils.EMPTY_STRING );

        if ( config != null )
        {
            EntryFilter entryFilter = new EntryFilter(  );
            entryFilter.setIdDirectory( config.getIdDirectory(  ) );
            entryFilter.setIsGroup( EntryFilter.FILTER_FALSE );
            entryFilter.setIsComment( EntryFilter.FILTER_FALSE );

            for ( IEntry entry : EntryHome.getEntryList( entryFilter, pluginDirectory ) )
            {
                int nIdEntryType = entry.getEntryType(  ).getIdType(  );

                if ( isEntryTypeAccepted( nIdEntryType ) )
                {
                    StringBuilder sbReferenceEntry = new StringBuilder(  );
                    sbReferenceEntry.append( entry.getPosition(  ) );
                    sbReferenceEntry.append( NotifyCRMConstants.SPACE + NotifyCRMConstants.OPEN_BRACKET );
                    sbReferenceEntry.append( I18nService.getLocalizedString( 
                            NotifyCRMConstants.PROPERTY_LABEL_REFERENCED_ENTRY, locale ) );
                    sbReferenceEntry.append( entry.getTitle(  ) );
                    sbReferenceEntry.append( NotifyCRMConstants.SPACE + NotifyCRMConstants.HYPHEN +
                        NotifyCRMConstants.SPACE );
                    sbReferenceEntry.append( I18nService.getLocalizedString( 
                            entry.getEntryType(  ).getTitleI18nKey(  ), locale ) );
                    sbReferenceEntry.append( NotifyCRMConstants.CLOSED_BRACKET );

                    refenreceListEntries.addItem( entry.getPosition(  ), sbReferenceEntry.toString(  ) );
                }
            }
        }

        return refenreceListEntries;
    }

    /**
     * Get the list of entries that have not the refused type (which are defined in the <b>workflow-notifycrm.properties</b>)
     * @param nIdTask the id task
     * @return a list of IEntry
     */
    public List<IEntry> getListEntries( int nIdTask )
    {
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        TaskNotifyCRMConfig config = TaskNotifyCRMConfigService.getService(  ).findByPrimaryKey( nIdTask );

        List<IEntry> listEntries = new ArrayList<IEntry>(  );

        if ( config != null )
        {
            EntryFilter entryFilter = new EntryFilter(  );
            entryFilter.setIdDirectory( config.getIdDirectory(  ) );

            for ( IEntry entry : EntryHome.getEntryList( entryFilter, pluginDirectory ) )
            {
                int nIdEntryType = entry.getEntryType(  ).getIdType(  );

                if ( !isEntryTypeRefused( nIdEntryType ) )
                {
                    listEntries.add( entry );
                }
            }
        }

        return listEntries;
    }

    /**
     * Get the id demand
     * @param config the config
     * @param resourceHistory the resource history
     * @param nIdRecord the id record
     * @param nIdDirectory the id directory
     * @return the id demand
     */
    public String getIdDemand( TaskNotifyCRMConfig config, ResourceHistory resourceHistory, int nIdRecord,
        int nIdDirectory )
    {
        String strReceiver = StringUtils.EMPTY;
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        // RecordField Id demand
        EntryFilter entryFilterIdDemand = new EntryFilter(  );
        entryFilterIdDemand.setPosition( config.getPositionEntryDirectoryIdDemand(  ) );
        entryFilterIdDemand.setIdDirectory( nIdDirectory );

        List<IEntry> listEntriesIdDemand = EntryHome.getEntryList( entryFilterIdDemand, pluginDirectory );

        if ( ( listEntriesIdDemand != null ) && !listEntriesIdDemand.isEmpty(  ) )
        {
            IEntry entryUserGuid = listEntriesIdDemand.get( 0 );
            RecordFieldFilter recordFieldFilterEmail = new RecordFieldFilter(  );
            recordFieldFilterEmail.setIdDirectory( nIdDirectory );
            recordFieldFilterEmail.setIdEntry( entryUserGuid.getIdEntry(  ) );
            recordFieldFilterEmail.setIdRecord( nIdRecord );

            List<RecordField> listRecordFieldsUserGuid = RecordFieldHome.getRecordFieldList( recordFieldFilterEmail,
                    pluginDirectory );

            if ( ( listRecordFieldsUserGuid != null ) && !listRecordFieldsUserGuid.isEmpty(  ) &&
                    ( listRecordFieldsUserGuid.get( 0 ) != null ) )
            {
                RecordField recordFieldIdDemand = listRecordFieldsUserGuid.get( 0 );
                strReceiver = recordFieldIdDemand.getValue(  );

                if ( recordFieldIdDemand.getField(  ) != null )
                {
                    strReceiver = recordFieldIdDemand.getField(  ).getTitle(  );
                }
            }
        }

        return strReceiver;
    }

    /**
     * Fill the model for the notification message
     * @param config the config
     * @param resourceHistory the resource history
     * @param record the record
     * @param directory the directory
     * @return the model filled
     */
    public Map<String, String> fillModel( TaskNotifyCRMConfig config, ResourceHistory resourceHistory, Record record,
        Directory directory )
    {
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        Map<String, String> model = new HashMap<String, String>(  );
        model.put( NotifyCRMConstants.MARK_MESSAGE, config.getMessage(  ) );
        model.put( NotifyCRMConstants.MARK_DIRECTORY_TITLE, directory.getTitle(  ) );
        model.put( NotifyCRMConstants.MARK_DIRECTORY_DESCRIPTION, directory.getDescription(  ) );

        RecordFieldFilter recordFieldFilter = new RecordFieldFilter(  );
        recordFieldFilter.setIdRecord( record.getIdRecord(  ) );

        List<RecordField> listRecordField = RecordFieldHome.getRecordFieldList( recordFieldFilter, pluginDirectory );

        for ( RecordField recordField : listRecordField )
        {
            String value = recordField.getValue(  );

            if ( isEntryTypeRefused( recordField.getEntry(  ).getEntryType(  ).getIdType(  ) ) )
            {
                continue;
            }
            else if ( ( recordField.getField(  ) != null ) &&
                    !( recordField.getEntry(  ) instanceof fr.paris.lutece.plugins.directory.business.EntryTypeGeolocation ) )
            {
                recordFieldFilter.setIdEntry( recordField.getEntry(  ).getIdEntry(  ) );
                listRecordField = RecordFieldHome.getRecordFieldList( recordFieldFilter, pluginDirectory );

                if ( ( listRecordField.get( 0 ) != null ) && ( listRecordField.get( 0 ).getField(  ) != null ) &&
                        ( listRecordField.get( 0 ).getField(  ).getTitle(  ) != null ) )
                {
                    value = listRecordField.get( 0 ).getField(  ).getTitle(  );
                }
            }
            else if ( recordField.getEntry(  ) instanceof fr.paris.lutece.plugins.directory.business.EntryTypeGeolocation &&
                    !recordField.getField(  ).getTitle(  ).equals( EntryTypeGeolocation.CONSTANT_ADDRESS ) )
            {
                continue;
            }

            recordField.setEntry( EntryHome.findByPrimaryKey( recordField.getEntry(  ).getIdEntry(  ), pluginDirectory ) );
            model.put( NotifyCRMConstants.MARK_POSITION + String.valueOf( recordField.getEntry(  ).getPosition(  ) ),
                value );
        }

        if ( ( directory.getIdWorkflow(  ) != DirectoryUtils.CONSTANT_ID_NULL ) &&
                WorkflowService.getInstance(  ).isAvailable(  ) )
        {
            State state = WorkflowService.getInstance(  )
                                         .getState( record.getIdRecord(  ), Record.WORKFLOW_RESOURCE_TYPE,
                    directory.getIdWorkflow(  ), null, null );
            model.put( NotifyCRMConstants.MARK_STATUS, state.getName(  ) );
        }

        return model;
    }
}
