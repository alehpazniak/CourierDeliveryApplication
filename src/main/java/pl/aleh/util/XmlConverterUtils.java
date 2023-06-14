package pl.aleh.util;

import static javax.xml.soap.SOAPConstants.SOAP_1_1_PROTOCOL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

@Slf4j
@UtilityClass
public final class XmlConverterUtils {

  private static final XmlMapper xmlMapper = new XmlMapper();

  @SneakyThrows
  public static String convertToXml(final Object object) {
    var context = JAXBContext.newInstance(object.getClass());
    var marshaller = context.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    var sw = new StringWriter();
    marshaller.marshal(object, sw);
    return sw.toString();
  }

  public static <T> T convertFromXml(final String xml, final Class<T> clazz) throws JAXBException {
    var context = JAXBContext.newInstance(clazz);
    var unmarshaller = context.createUnmarshaller();

    var sr = new StringReader(xml);
    return clazz.cast(unmarshaller.unmarshal(sr));
  }

  public static <T> T convertFromXml(final InputStream inputStream, final Class<T> clazz) throws JAXBException {
    var context = JAXBContext.newInstance(clazz);
    var unmarshaller = context.createUnmarshaller();

    return clazz.cast(unmarshaller.unmarshal(inputStream));
  }

  public static <T> T strictConvertFromXml(final String xml, final Class<T> clazz) throws JAXBException {
    var context = JAXBContext.newInstance(clazz);
    var unmarshaller = context.createUnmarshaller();

    var sr = new StringReader(xml);
    return clazz.cast(unmarshaller.unmarshal(new StreamSource(sr), clazz).getValue());
  }

  public static String getXmlAsJsonString(final String xml) {
    Object xmlObject = null;
    try {
      xmlObject = xmlMapper.readValue(xml, Object.class);
    } catch (JsonProcessingException e) {
      log.error("Unable to parse XML string into Object", e);
    }
    return xmlObject == null ? null : xmlObject.toString();
  }

  public static <T> void marshallToResult(JAXBElement<T> element, Result result) throws JAXBException {
    var context = JAXBContext.newInstance(element.getDeclaredType());
    var marshaller = context.createMarshaller();
    marshaller.marshal(element, result);
  }

  public static SOAPMessage toSoapMessage(final String soapProtocol, final String xml)
      throws SOAPException, IOException {
    var factory = MessageFactory.newInstance(soapProtocol);
    var stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    return factory.createMessage(new MimeHeaders(), stream);
  }

  public static SOAPMessage toSoapMessage(final InputStream is) throws SOAPException, IOException {
    var factory = MessageFactory.newInstance();
    return factory.createMessage(new MimeHeaders(), is);
  }

  public static <T> T unmarshall(final String soapMessageString, final Class<T> clazz)
      throws IOException, SOAPException, JAXBException {
    return unmarshall(SOAP_1_1_PROTOCOL, soapMessageString, clazz);
  }

  public static <T> T unmarshall(final String soapProtocol, final String soapMessageString, final Class<T> clazz)
      throws IOException, SOAPException, JAXBException {
    var message = toSoapMessage(soapProtocol, soapMessageString);
    var soapBodyDoc = message.getSOAPBody().extractContentAsDocument();
    return unmarshall(soapBodyDoc, clazz);
  }

  public static <T> T unmarshall(final Document soapBodyDoc, final Class<T> clazz) throws JAXBException {
    var jaxbContext = JAXBContext.newInstance(clazz);
    var jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    var root = jaxbUnmarshaller.unmarshal(soapBodyDoc, clazz);
    return root.getValue();
  }

  public static <T> T unmarshall(final Path soapMessagePath, final Class<T> clazz)
      throws IOException, SOAPException, JAXBException {
    try (var is = Files.newInputStream(soapMessagePath)) {
      var message = toSoapMessage(is);
      return unmarshall(message.getSOAPBody().extractContentAsDocument(), clazz);
    }
  }

  public static <T> T unmarshall(final URL soapMessageUrl, final Class<T> clazz)
      throws IOException, SOAPException, JAXBException {
    try (var is = soapMessageUrl.openStream()) {
      var message = toSoapMessage(is);
      return unmarshall(message.getSOAPBody().extractContentAsDocument(), clazz);
    }
  }

  public static <T> T unmarshall(final InputStream soapMessageInputStream, final Class<T> clazz)
      throws IOException, SOAPException, JAXBException {
    var message = toSoapMessage(soapMessageInputStream);
    var soapBodyDoc = message.getSOAPBody().extractContentAsDocument();
    return unmarshall(soapBodyDoc, clazz);
  }

  public static <T> List<T> unmarshallListOrElseEmpty(final String xml, final Class<T> clazz) {
    try {
      return xmlMapper.readValue(xml, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz));
    } catch (JsonProcessingException e) {
      return List.of();
    }
  }

  public static <T> List<T> getAsList(final String xml, final Class<T> clazz) throws JsonProcessingException {
    return xmlMapper.readValue(xml, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz));
  }

}
