package utilities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyBasicNameValuePair  {
  //TODO bez teto tridy
  private String name;
  private String value;
  
  public MyBasicNameValuePair() {
    super();
  }
  
  public MyBasicNameValuePair(String name, String value) {
    super();
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "MyBasicNameValuePair [name=" + name + ", value=" + value + "]";
  }
  
  
  
  

 
  
  

}
