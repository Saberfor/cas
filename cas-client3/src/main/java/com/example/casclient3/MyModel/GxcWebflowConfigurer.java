package com.example.casclient3.MyModel;

import org.apereo.cas.authentication.RememberMeUsernamePasswordCredential;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.configurer.DefaultWebflowConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.engine.builder.BinderConfiguration;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

public class GxcWebflowConfigurer extends DefaultWebflowConfigurer {

    public GxcWebflowConfigurer(FlowBuilderServices flowBuilderServices, FlowDefinitionRegistry flowDefinitionRegistry, ApplicationContext applicationContext, CasConfigurationProperties casProperties) {
        super(flowBuilderServices, flowDefinitionRegistry, applicationContext, casProperties);
    }

    @Override
    protected void createRememberMeAuthnWebflowConfig(Flow flow) {
        if (this.casProperties.getTicket().getTgt().getRememberMe().isEnabled()) {
            this.createFlowVariable(flow, "credential", RememberMeUsernamePasswordCredential.class);
            ViewState state = (ViewState)this.getState(flow, "viewLoginForm", ViewState.class);
            BinderConfiguration cfg = this.getViewStateBinderConfiguration(state);
            cfg.addBinding(new BinderConfiguration.Binding("rememberMe", (String)null, false));
        } else {
            this.createFlowVariable(flow, "credential", MyUsernamePasswordCredential.class);
            ViewState state = (ViewState)this.getState(flow, "viewLoginForm", ViewState.class);
            BinderConfiguration cfg = this.getViewStateBinderConfiguration(state);
            cfg.addBinding(new BinderConfiguration.Binding("capcha", (String)null, true));
        }

    }
}
