/**
 *
 */
package de.xwic.sandbox.demoapp.ui.editext.companies;

import de.jwic.base.IControl;
import de.jwic.controls.InputBox;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.IEntityModel;
import de.xwic.appkit.webbase.editors.EditorContext;
import de.xwic.appkit.webbase.editors.events.EditorAdapter;
import de.xwic.appkit.webbase.editors.events.EditorEvent;
import de.xwic.appkit.webbase.editors.events.EditorListener;
import de.xwic.appkit.webbase.editors.events.IEditorListenerFactory;
import de.xwic.sandbox.demoapp.model.entities.ICompany;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This extension demonstrates how a generic entity editor can be extended. The
 * extension needs to be configured in the <code>extension.xml</code> file in the
 * root of the source folder of this module.
 *
 * @author Vitaliy Zhovtyuk
 */
public class CompanyBeforeSaveListenerExtension implements IEditorListenerFactory {

    protected final static Log log = LogFactory.getLog(CompanyBeforeSaveListenerExtension.class);

    /**
     *
     */
    public CompanyBeforeSaveListenerExtension() {
    }

    @Override
    public EditorListener createListener() {
        return new EditorAdapter() {
            @Override
            public void beforeSave(EditorEvent event) {
                if (event.getSource() instanceof EditorContext) {
                    EditorContext editorContext = (EditorContext) event.getSource();
                    
                    String userName = DAOSystem.getSecurityManager().getCurrentUser().getLogonName();
                    
                    InputBox notesField = (InputBox)editorContext.getControlById("notes");
                    
                    notesField.setText("Updated by " + userName + "\n" + notesField.getText());
                    
                    
                    
                    
                }
            }
        };
    }
}
