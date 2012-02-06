package se.kodapan.geography.geocoding;

import com.sleepycat.persist.model.Persistent;

/**
 * @author kalle
 * @since 2012-02-06 20:24
 */
@Persistent
public class CachedGeocoderException {

  private String message;
  private String[] classNames;
  private String[] fileNames;
  private String[] methodNames;
  private int[] lineNumbers;


  public CachedGeocoderException() {
  }

  public CachedGeocoderException(Exception exception) {
    message = exception.getMessage();
    classNames = new String[exception.getStackTrace().length];
    fileNames = new String[exception.getStackTrace().length];
    methodNames = new String[exception.getStackTrace().length];
    lineNumbers = new int[exception.getStackTrace().length];

    StackTraceElement[] stackTrace1 = exception.getStackTrace();
    for (int i = 0, stackTrace1Length = stackTrace1.length; i < stackTrace1Length; i++) {
      StackTraceElement element = stackTrace1[i];
      classNames[i] = element.getClassName();
      fileNames[i] = element.getFileName();
      methodNames[i] = element.getMethodName();
      lineNumbers[i] = element.getLineNumber();
    }
  }

  public Exception toException(Throwable cause) {
    Exception exception = new Exception(message, cause);
    StackTraceElement[] stackTraceElements = new StackTraceElement[classNames.length];
    for (int i=0;i<stackTraceElements.length; i++) {
      stackTraceElements[i] = new StackTraceElement(classNames[i], methodNames[i], fileNames[i], lineNumbers[i]);
    }
    return exception;
  }

  @Override
  public String toString() {
    return "CachedGeocoderException{" +
        "message='" + message + '\'' +
        ", stackTrace.length='" + classNames.length + '\'' +
        '}';
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String[] getClassNames() {
    return classNames;
  }

  public void setClassNames(String[] classNames) {
    this.classNames = classNames;
  }

  public String[] getFileNames() {
    return fileNames;
  }

  public void setFileNames(String[] fileNames) {
    this.fileNames = fileNames;
  }

  public String[] getMethodNames() {
    return methodNames;
  }

  public void setMethodNames(String[] methodNames) {
    this.methodNames = methodNames;
  }

  public int[] getLineNumbers() {
    return lineNumbers;
  }

  public void setLineNumbers(int[] lineNumbers) {
    this.lineNumbers = lineNumbers;
  }
}
