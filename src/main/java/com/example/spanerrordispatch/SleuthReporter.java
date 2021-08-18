package com.example.spanerrordispatch;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextChangedEvent;
import org.springframework.stereotype.Component;

@Component
public class SleuthReporter {

	private final Tracer tracer;

	private Span security;

	public SleuthReporter(Tracer tracer) {
		this.tracer = tracer;
	}

	@EventListener
	public void securityContextChanged(SecurityContextChangedEvent event) {
		SecurityContext previousContext = event.getPreviousContext();
		SecurityContext currentContext = event.getCurrentContext();
		Authentication previous = previousContext == null ? null : previousContext.getAuthentication();
		Authentication current = currentContext == null ? null : currentContext.getAuthentication();
		if (previous == null && current == null) {
			return;
		}
		if (previous == null) {
			this.security = this.tracer.nextSpan()
					.name("authenticated")
					.tag("authentication.type", getAuthenticationType(current))
					.tag("authentication.authorities", String.valueOf(current.getAuthorities()))
					.start();
			return;
		}
		if (current == null) {
			this.security.end();
			return;
		}
		throw new IllegalStateException("For the use case the sample covers, this should not be possible.");
	}

	private String getAuthenticationType(Authentication authentication) {
		String name = authentication.getClass().getSimpleName();
		if (name.endsWith("AuthenticationToken")) {
			return name.substring(0, name.lastIndexOf("AuthenticationToken"));
		}
		if (name.endsWith("Authentication")) {
			return name.substring(0, name.lastIndexOf("Authentication"));
		}
		return name;
	}
}
