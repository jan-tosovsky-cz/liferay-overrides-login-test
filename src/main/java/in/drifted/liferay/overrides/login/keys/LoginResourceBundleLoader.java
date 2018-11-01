package in.drifted.liferay.overrides.login.keys;

import com.liferay.portal.kernel.util.AggregateResourceBundleLoader;
import com.liferay.portal.kernel.util.CacheResourceBundleLoader;
import com.liferay.portal.kernel.util.ClassResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import java.util.Locale;
import java.util.ResourceBundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = {
            "bundle.symbolic.name=com.liferay.login.web",
            "resource.bundle.base.name=content.Language",
            "servlet.context.name=login-web"
        }
)
public class LoginResourceBundleLoader implements ResourceBundleLoader {

    @Override
    public ResourceBundle loadResourceBundle(Locale locale) {

        System.out.println("loadResourceBundle: " + locale.toString());

        int i = 0;
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            System.out.println(i + ": " + element.toString());
            if (i > 10) {
                break;
            }
            i++;
        }

        return _resourceBundleLoader.loadResourceBundle(locale);
    }

    @Reference(
            target = "(&(bundle.symbolic.name=com.liferay.login.web)(!(component.name=in.drifted.liferay.overrides.login.keys.LoginResourceBundleLoader)))"
    )
    public void setResourceBundleLoader(ResourceBundleLoader theirsResourceBundleLoader) {

        ResourceBundleLoader mineResourceBundleLoader = new CacheResourceBundleLoader(new ClassResourceBundleLoader("content.Language", LoginResourceBundleLoader.class.getClassLoader()));
        ResourceBundle mineResourceBundle = mineResourceBundleLoader.loadResourceBundle(Locale.US);

        for (String key : mineResourceBundle.keySet()) {
            if (key.equals("javax.portlet.title.com_liferay_login_web_portlet_LoginPortlet")) {
                System.out.println("mine: " + mineResourceBundle.getString(key));
            }
        }

        ResourceBundle theirsResourceBundle = theirsResourceBundleLoader.loadResourceBundle(Locale.US);
        for (String key : theirsResourceBundle.keySet()) {
            if (key.equals("javax.portlet.title.com_liferay_login_web_portlet_LoginPortlet")) {
                System.out.println("theirs: " + theirsResourceBundle.getString(key));
            }
        }

        ResourceBundleLoader combineResourceBundleLoader = new AggregateResourceBundleLoader(mineResourceBundleLoader, theirsResourceBundleLoader);
        ResourceBundle combinedResourceBundle = combineResourceBundleLoader.loadResourceBundle(Locale.US);

        for (String key : combinedResourceBundle.keySet()) {
            if (key.equals("javax.portlet.title.com_liferay_login_web_portlet_LoginPortlet")) {
                System.out.println("combined: " + combinedResourceBundle.getString(key));
            }
        }

        //_resourceBundleLoader = mineResourceBundleLoader;
        _resourceBundleLoader = theirsResourceBundleLoader;
        //_resourceBundleLoader = combineResourceBundleLoader;
    }

    private ResourceBundleLoader _resourceBundleLoader;

}
