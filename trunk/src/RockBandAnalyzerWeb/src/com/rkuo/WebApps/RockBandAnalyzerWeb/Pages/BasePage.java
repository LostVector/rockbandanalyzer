package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.LoginHeaderPanel;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.NavigationHeaderPanel;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.FooterPanel;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.CacheException;
import java.util.*;

public abstract class BasePage extends WebPage {
    public BasePage() {
//		add(new BookmarkablePageLink("page1", Page1.class));
//		add(new BookmarkablePageLink("page2", Page2.class));
//		add(new Label("footer", "This is in the footer"));
        add(new LoginHeaderPanel("lhpHeader"));
        add(new NavigationHeaderPanel("nhpHeader"));
        add(new FooterPanel("fpFooter"));

//        AddAutoComplete();
        return;
    }

    protected static Cache getCache() {
        Cache cache;

        try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
        }
        catch( CacheException e ) {
            return null;
        }

        return cache;
    }
    
    protected void AddAutoComplete() {

        Form<Void> form = new Form<Void>("form");
        add(form);

        final AutoCompleteTextField<String> field = new AutoCompleteTextField<String>("ac",
                new Model<String>("")) {
            @Override
            protected Iterator<String> getChoices(String input) {
                if( Strings.isEmpty(input) ) {
                    List<String> emptyList = Collections.emptyList();
                    return emptyList.iterator();
                }

                List<String> choices = new ArrayList<String>(10);

                Locale[] locales = Locale.getAvailableLocales();

                for( final Locale locale : locales ) {
                    final String country = locale.getDisplayCountry();

                    if( country.toUpperCase().startsWith(input.toUpperCase()) ) {
                        choices.add(country);
                        if( choices.size() == 10 ) {
                            break;
                        }
                    }
                }

                return choices.iterator();
            }
        };

        form.add(field);

//        final Label label = new Label("selectedValue", field.getDefaultModel());
//        label.setOutputMarkupId(true);
//        form.add(label);

//        ACAjaxFormSubmitBehavior    behavior;
//        behavior = new ACAjaxFormSubmitBehavior(form, "onchange");
//        behavior.setLabel( label );
//        field.add( behavior );

        return;
    }

    private class ACAjaxFormSubmitBehavior extends AjaxFormSubmitBehavior {

        public Label getLabel() {
            return _label;
        }

        public void setLabel(Label label) {
            _label = label;
        }

        private Label _label;

        public ACAjaxFormSubmitBehavior( Form form, String s ) {
            super( form, s );
            return;
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            target.addComponent(_label);
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
        }
    }
}
