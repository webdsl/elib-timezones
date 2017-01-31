module elib/elib-timezones

section Date/DateTime extensions

type DateTime{
  //the timezone argument is the local timezone in both cases
  org.webdsl.utils.TimeZoneUtil.toLocalTime as toLocalTime(TimeZone) : DateTime
  org.webdsl.utils.TimeZoneUtil.toServerTime as toServerTime(TimeZone) : DateTime
}

native class java.util.TimeZone as TimeZone{
  getDisplayName() : String
  getID() : String
  static getTimeZone(String) : TimeZone
}

native class org.webdsl.utils.TimeZoneUtil as TimeZoneUtil{
  static toServerTime(Date, TimeZone) : Date
  static toLocalTime(Date, TimeZone) : Date
  static timeZoneIds() : List<String>
  static timeZoneLabels() : List<String>
  static getServerTimeZone() : TimeZone
}

section templates

override template dateoutputgeneric( d: ref Date, defaultformat: String ){
  var dateformat := defaultformat
  var timezone : TimeZone
  init{
    if(d != null){
      timezone := getViewTimeZone(d.getEntity());
      //@TODO add support for ref arg in function, to avoid repeating this in both output and input
      var attr := attribute( "format" );
      if(    attr != null
          && attr != ""
      ){
        dateformat := attr;
      }
      else{
        if( d.getReflectionProperty() != null ){
          var formatanno := d.getReflectionProperty().getFormatAnnotation();
          if( formatanno != null ){
            dateformat := formatanno;
          }
        }
      }
    }
  }
  if(d != null){ output( d.toLocalTime(timezone).format( dateformat ) ) output(timezone) }
}

override template datepickerinput( d: ref Date, dateformat: String, tname: String, options: String ){
  var s: String
  var momentJSFormat := convertJavaDateFormatToMomentJS(dateformat)
  var req := getRequestParameter( tname )
  var onOpen := "onOpen: function(dateObj, dateStr, instance){ if(dateStr == ''){ instance.jumpToDate( new Date() ); } }"
  var timezone : TimeZone
  init{
    if( d == null ){
      s := "";
      timezone := getViewTimeZone(null);
    }
    else{
      timezone := getViewTimeZone(d.getEntity());
      s := d.toLocalTime(timezone).format( dateformat );
      
    }
    if(req != null){
      s := req;
    }
  }
  datepickerIncludes

  <input
    if( getPage().inLabelContext() ){
      id = getPage().getLabelString()
    }
    class = "flatpickr"
    name = tname
    type = "text"
    value = s
    inputDate attributes
    all attributes
    title = timezone.getDisplayName()
  />
  output(timezone)

  <script>
    $("input:not(.flatpickr-input)[name=~tname]").flatpickr({~onOpen, allowInput: true, parseDate:function(str){ return moment(str, "~(momentJSFormat))").toDate(); }, time_24hr: true, ~options});
  </script>

  databind{
    if( req != null ){
      if( req == "" ){
        d := null;
      }
      else{
        var newdate := req.parseDateTime( dateformat );
        if( newdate != null ){
          newdate := newdate.toServerTime(timezone);
          d := newdate;
        }
      }
    }
  }
}

template output(tz : TimeZone){
  span[class="timezone-info"]{ " " output( tz.getDisplayName() ) }
}

template inputTimeZone( prop : ref String ){
  var fromValues := TimeZoneUtil.timeZoneIds()
  var fromLabels := TimeZoneUtil.timeZoneLabels()
  var tname := getTemplate().getUniqueId()
  
  var req := getRequestParameter(tname)
  if(fromValues.length != fromLabels.length){
    "Error loading input"
  } else {
    <select
      name = tname
      if(getPage().inLabelContext()) {
        id=getPage().getLabelString()
      }
      inputSelect attributes>
      
        for( idx : Int from 0 to fromValues.length ){
          <option
            value=fromValues[idx]
            if( fromValues[idx]==prop ){
              selected="selected"
            }
          >
            output( fromLabels[idx] )
            
          </option>
        }   
      
    </select>
    
    databind{
      if(fromValues.indexOf(req) > -1){ //only allow an option that is in list 'from'
        prop := req;
      }
    }
  }
}