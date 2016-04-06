/**
 *
 */
package de.xwic.sandbox.demoapp.ui.editext.companies;

import de.xwic.appkit.core.model.IEntityModel;
import de.xwic.appkit.webbase.editors.EditorContext;
import de.xwic.appkit.webbase.editors.events.EditorAdapter;
import de.xwic.appkit.webbase.editors.events.EditorEvent;
import de.xwic.appkit.webbase.editors.events.EditorListener;
import de.xwic.appkit.webbase.editors.events.IEditorListenerFactory;
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
                    IEntityModel model = editorContext.getModel();
                    String website;
                    try {
                        website = String.valueOf(model.getProperty("webSite"));
                    } catch (Exception e) {
                        log.error("Failed to read property " + e.getMessage(), e);
                        throw new RuntimeException(e.getMessage(), e);
                    }

                    try {
                        InetAddress inetAddress = InetAddress.getByName(website);
                        assert inetAddress != null;
                    } catch (UnknownHostException e) {
                        throw new RuntimeException("Host " + website + " does not exists");
                    }
                }
            }
        };
    }
}
