package xdi2.connector.meeco.contributor;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import xdi2.connector.meeco.api.MeecoApi;
import xdi2.connector.meeco.mapping.MeecoMapping;
import xdi2.connector.meeco.util.GraphUtil;
import xdi2.core.ContextNode;
import xdi2.core.Graph;
import xdi2.core.syntax.XDIAddress;
import xdi2.core.syntax.XDIStatement;
import xdi2.messaging.MessageEnvelope;
import xdi2.messaging.operations.GetOperation;
import xdi2.messaging.operations.SetOperation;
import xdi2.messaging.container.MessagingContainer;
import xdi2.messaging.container.Prototype;
import xdi2.messaging.container.contributor.ContributorMount;
import xdi2.messaging.container.contributor.ContributorResult;
import xdi2.messaging.container.contributor.impl.AbstractContributor;
import xdi2.messaging.container.exceptions.Xdi2MessagingException;
import xdi2.messaging.container.execution.ExecutionContext;
import xdi2.messaging.container.execution.ExecutionResult;
import xdi2.messaging.container.impl.graph.GraphMessagingContainer;
import xdi2.messaging.container.interceptor.InterceptorResult;
import xdi2.messaging.container.interceptor.MessageEnvelopeInterceptor;

@ContributorMount(contributorXDIAddresses={"{}#meeco"})
public class MeecoContributor extends AbstractContributor implements MessageEnvelopeInterceptor, Prototype<MeecoContributor> {

	private static final Logger log = LoggerFactory.getLogger(MeecoContributor.class);

	private MeecoApi meecoApi;
	private MeecoMapping meecoMapping;
	private Graph tokenGraph;

	public MeecoContributor() {

		super();

		this.meecoApi = null;
		this.meecoMapping = null;
		this.tokenGraph = null;
	}

	/*
	 * Prototype
	 */

	@Override
	public MeecoContributor instanceFor(PrototypingContext prototypingContext) throws Xdi2MessagingException {

		// create new contributor

		MeecoContributor contributor = new MeecoContributor();

		// set the graph

		contributor.setTokenGraph(this.getTokenGraph());

		// set api and mapping

		contributor.setMeecoApi(this.getMeecoApi());
		contributor.setMeecoMapping(this.getMeecoMapping());

		// done

		return contributor;
	}

	/*
	 * Init and shutdown
	 */

	@Override
	public void init(MessagingContainer messagingContainer) throws Exception {

		super.init(messagingContainer);

		if (this.getTokenGraph() == null && messagingContainer instanceof GraphMessagingContainer) this.setTokenGraph(((GraphMessagingContainer) messagingContainer).getGraph()); 
		if (this.getTokenGraph() == null) throw new Xdi2MessagingException("No token graph.", null, null);
	}

	/*
	 * Contributor
	 */

	@Override
	public ContributorResult executeSetOnLiteralStatement(XDIAddress[] contributorXDIAddresses, XDIAddress contributorsXDIAddress, XDIStatement relativeTargetStatement, SetOperation operation, Graph operationResultGraph, ExecutionContext executionContext) throws Xdi2MessagingException {

		XDIAddress meecoUserXDIAddress = contributorXDIAddresses[contributorXDIAddresses.length - 1];

		log.debug("meecoUserXDIAddress: " + meecoUserXDIAddress);

		if (meecoUserXDIAddress.equals("{}#meeco")) return ContributorResult.DEFAULT;

		// retrieve the Meeco user

		Map<XDIAddress, JsonObject> user = null;

		try {

			String meecoEmail = GraphUtil.retrieveMeecoEmail(MeecoContributor.this.getTokenGraph(), meecoUserXDIAddress);
			if (meecoEmail == null) {

				log.warn("No Meeco email for context: " + meecoUserXDIAddress);
				return new ContributorResult(true, false, true);
			}

			String meecoPassword = GraphUtil.retrieveMeecoPassword(MeecoContributor.this.getTokenGraph(), meecoUserXDIAddress);
			if (meecoPassword == null) {

				log.warn("No Meeco password for context: " + meecoUserXDIAddress);
				return new ContributorResult(true, false, true);
			}

			user = MeecoContributor.this.retrieveUser(executionContext, meecoEmail, meecoPassword);
			if (user == null) throw new Exception("No user.");
		} catch (Exception ex) {

			throw new Xdi2MessagingException("Cannot load user data: " + ex.getMessage(), ex, executionContext);
		}

		// modify user

		try {

			String value = relativeTargetStatement.getLiteralData().toString();
			String oldvalue = user.get(relativeTargetStatement.getContextNodeXDIAddress()).get("value").getAsString();
			if (relativeTargetStatement.getContextNodeXDIAddress().toString().equals("<#first><#name>")) {
				if (oldvalue.contains(" ")) oldvalue = oldvalue.substring(oldvalue.indexOf(' ')+1);
				value = value + " " + oldvalue;
			}
			if (relativeTargetStatement.getContextNodeXDIAddress().toString().equals("<#last><#name>")) {
				if (oldvalue.contains(" ")) oldvalue = oldvalue.substring(0, oldvalue.indexOf(' '));
				value = oldvalue + " " + value;
			}

			user.get(relativeTargetStatement.getContextNodeXDIAddress()).addProperty("value", value);
			MeecoContributor.this.meecoApi.put(user);
		} catch (Exception ex) {

			throw new Xdi2MessagingException("Cannot save user data: " + ex.getMessage(), ex, executionContext);
		}

		// done

		return new ContributorResult(true, false, true);
	}

	@Override
	public ContributorResult executeGetOnAddress(XDIAddress[] contributorXDIAddresses, XDIAddress contributorsXDIAddress, XDIAddress relativeTargetAddress, GetOperation operation, Graph operationResultGraph, ExecutionContext executionContext) throws Xdi2MessagingException {

		XDIAddress meecoUserXDIAddress = contributorXDIAddresses[contributorXDIAddresses.length - 1];

		log.debug("GET meecoUserXDIAddress: " + meecoUserXDIAddress);

		if (meecoUserXDIAddress.equals("{}#meeco")) return ContributorResult.DEFAULT;

		// retrieve the Meeco user

		Map<XDIAddress, JsonObject> user = null;

		try {

			String meecoEmail = GraphUtil.retrieveMeecoEmail(MeecoContributor.this.getTokenGraph(), meecoUserXDIAddress);
			if (meecoEmail == null) {

				log.warn("No Meeco email for context: " + meecoUserXDIAddress);
				return new ContributorResult(true, false, true);
			}

			String meecoPassword = GraphUtil.retrieveMeecoPassword(MeecoContributor.this.getTokenGraph(), meecoUserXDIAddress);
			if (meecoPassword == null) {

				log.warn("No Meeco password for context: " + meecoUserXDIAddress);
				return new ContributorResult(true, false, true);
			}

			user = MeecoContributor.this.retrieveUser(executionContext, meecoEmail, meecoPassword);
			if (user == null) throw new Exception("No user.");
		} catch (Exception ex) {

			throw new Xdi2MessagingException("Cannot load user data: " + ex.getMessage(), ex, executionContext);
		}

		// add the user to the response

		if (user != null) {

			ContextNode contextNode = operationResultGraph.setDeepContextNode(contributorsXDIAddress);

			for (Entry<XDIAddress, JsonObject> entry : user.entrySet()) {

				String value = entry.getValue().get("value").getAsString();
				if (entry.getKey().toString().equals("<#first><#name>") && value.contains(" ")) value = value.substring(0, value.indexOf(' '));
				if (entry.getKey().toString().equals("<#last><#name>") && value.contains(" ")) value = value.substring(value.indexOf(' ')+1);

				contextNode.setDeepContextNode(entry.getKey()).setLiteralString(value);
			}
		}

		// done

		return new ContributorResult(true, false, true);
	}

	/*
	 * MessageEnvelopeInterceptor
	 */

	@Override
	public InterceptorResult before(MessageEnvelope messageEnvelope, ExecutionContext executionContext, ExecutionResult executionResult) throws Xdi2MessagingException {

		MeecoContributorExecutionContext.resetUsers(executionContext);

		return InterceptorResult.DEFAULT;
	}

	@Override
	public InterceptorResult after(MessageEnvelope messageEnvelope, ExecutionContext executionContext, ExecutionResult executionResult) throws Xdi2MessagingException {

		return InterceptorResult.DEFAULT;
	}

	@Override
	public void exception(MessageEnvelope messageEnvelope, ExecutionContext executionContext, ExecutionResult executionResult, Exception ex) {

	}

	/*
	 * Helper methods
	 */

	private Map<XDIAddress, JsonObject> retrieveUser(ExecutionContext executionContext, String email, String password) throws IOException, JSONException {

		Map<XDIAddress, JsonObject> user = MeecoContributorExecutionContext.getUser(executionContext, email);

		if (user == null) {

			user = this.meecoApi.get(email, password);

			MeecoContributorExecutionContext.putUser(executionContext, email, user);
		}

		return user;
	}

	/*
	 * Getters and setters
	 */

	public MeecoApi getMeecoApi() {

		return this.meecoApi;
	}

	public void setMeecoApi(MeecoApi meecoApi) {

		this.meecoApi = meecoApi;
	}

	public MeecoMapping getMeecoMapping() {

		return this.meecoMapping;
	}

	public void setMeecoMapping(MeecoMapping meecoMapping) {

		this.meecoMapping = meecoMapping;
	}

	public Graph getTokenGraph() {

		return this.tokenGraph;
	}

	public void setTokenGraph(Graph tokenGraph) {

		this.tokenGraph = tokenGraph;
	}
}
