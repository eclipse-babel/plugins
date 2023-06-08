package org.eclipse.babel.editor.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.babel.core.message.IMessage;
import org.eclipse.babel.core.message.IMessagesBundle;
import org.eclipse.babel.core.message.internal.Message;
import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.pde.nls.internal.ui.parser.LocaleUtil;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * <resource-bundle>
 *    <message-key key="hello.label">
 *       <message locale="en_GB" value="Hello world!" comment="Mandatory phrase"/>
 *       <message locale="sv_SE" value="Hej vÃ¤rlden!"/>
 *    </message-key>
 * </resource-bundle>
 */
public class ClipboardUtil {

	
	public static String serializeKeys(MessagesBundleGroup bundleGroup, String[] keys)
	{
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("resource-bundle");
			doc.appendChild(rootElement);
 
			for(String messageKey : keys) {
				IMessage[] messages = bundleGroup.getMessages(messageKey);
				if(messages != null && messages.length > 0) {
					Element messageKeyElement = doc.createElement("message-key");	//$NON-NLS-1$
					messageKeyElement.setAttribute("key", messageKey);	//$NON-NLS-1$
					rootElement.appendChild(messageKeyElement);
					for(IMessage message : messages) {
			    		Element messageElement = doc.createElement("message");	//$NON-NLS-1$

			    		String stringLocale = Locale.ROOT.toString();
			    		Locale locale = message.getLocale();
			    		if(locale != null) {
				    		stringLocale = locale.toString();
			    		}
			    		messageElement.setAttribute("locale", stringLocale);	//$NON-NLS-1$
			    		messageElement.setAttribute("value", message.getValue());	//$NON-NLS-1$
			    		messageElement.setAttribute("comment", message.getComment());	//$NON-NLS-1$
			    		messageKeyElement.appendChild(messageElement);
			    	}

				}
			}
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
			return output;
		} catch (Exception e) {

		}

		return null;
	}
	
	public static void deserializeKeys(MessagesBundleGroup bundleGroup, String xmlContent)
	{
		// root elements

		try {
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(is);

			NodeList nodes = doc.getElementsByTagName("resource-bundle"); /* Root element, should only be one. */

			for (int i = 0; i < nodes.getLength(); i++) {
			  Element element = (Element) nodes.item(i);
			  NodeList messageKeyNodes = element.getElementsByTagName("message-key");
			  for(int messageKeyNodeIndex = 0; messageKeyNodeIndex < messageKeyNodes.getLength(); messageKeyNodeIndex++) {
				  Element messageKeyNode = (Element) messageKeyNodes.item(messageKeyNodeIndex);
				  String messageKey = messageKeyNode.getAttribute("key");	//$NON-NLS-1$
			      NodeList messageNodes = messageKeyNode.getElementsByTagName("message");
			      for(int messageNodeIndex = 0; messageNodeIndex < messageNodes.getLength(); messageNodeIndex++) {
			    	  Element messageElement = (Element) messageNodes.item(messageNodeIndex);
			    	  messageElement.getAttribute("locale");
			    	  String stringLocale = messageElement.getAttribute("locale");	//$NON-NLS-1$
			    	  String value = messageElement.getAttribute("value");	//$NON-NLS-1$
			    	  String comment = messageElement.getAttribute("comment");	//$NON-NLS-1$

			    	  Locale locale = null;		/* Empty locale is considered as Locale.ROOT */
			    	  if(stringLocale != null && stringLocale.isEmpty() == false) {
			    		  locale = LocaleUtil.parseLocale(stringLocale);
			    	  }
			    	  IMessagesBundle messagesBundle = bundleGroup.getMessagesBundle(locale);
			    	  if(messagesBundle == null) {
			    		  bundleGroup.addMessagesBundle(locale);
			    		  messagesBundle = bundleGroup.getMessagesBundle(locale);
			    	  }

			    	  IMessage currentMessage = messagesBundle.getMessage(messageKey);
			    	  if(currentMessage == null) {
			    		  currentMessage = new Message(messageKey, locale);
			    		  messagesBundle.addMessage(currentMessage);
			    	  }

			    	  currentMessage.setComment(comment);
			    	  currentMessage.setText(value);
			      }

			  }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void putOnClipboard(Display display, String content)
	{
		Clipboard clipboard = new Clipboard(display);

		clipboard.setContents(new Object[] {content},  new Transfer[] {TranslationTransfer.getInstance()});

		clipboard.dispose();
	}
	
	public static String getFromClipboard(Display display)
	{
		Clipboard clipboard = new Clipboard(display);

		String contents = (String) clipboard.getContents(TranslationTransfer.getInstance());

		clipboard.dispose();
		
		return contents;
	}

	
}
