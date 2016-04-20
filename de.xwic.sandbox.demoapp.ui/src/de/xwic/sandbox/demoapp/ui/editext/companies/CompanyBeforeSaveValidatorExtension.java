/**
 *
 */
package de.xwic.sandbox.demoapp.ui.editext.companies;

import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.model.IEntityModel;
import de.xwic.appkit.webbase.editors.IEditorValidatorListener;
import de.xwic.appkit.webbase.editors.events.ValidationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This extension demonstrates how a generic entity editor can be extended. The
 * extension needs to be configured in the <code>extension.xml</code> file in the
 * root of the source folder of this module.
 *
 * @author Vitaliy Zhovtyuk
 */
public class CompanyBeforeSaveValidatorExtension implements IEditorValidatorListener {

    protected final static Log log = LogFactory.getLog(CompanyBeforeSaveValidatorExtension.class);

    /**
     *
     */
    public CompanyBeforeSaveValidatorExtension() {
    }

    @Override
    public void modelValidated(ValidationEvent validationEvent) {
        final IEntityModel model = (IEntityModel) validationEvent.getEventSource();
        final ValidationResult validationResult = validationEvent.getValidationResult();
        if(validationEvent.isOnSave()) {
            final String website;
            try {
                website = String.valueOf(model.getProperty("webSite"));
            } catch (Exception e) {
                log.error("Failed to read property " + e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
            if (!website.startsWith("http")) {
                validationResult.addError("de.xwic.sandbox.demoapp.model.entities.ICompany.webSite",
                        "de.xwic.sandbox.demoapp.model.entities.ICompany.webSite.httpStart");
            }
        }
    }
}
