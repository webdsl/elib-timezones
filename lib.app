module elib/elib-timezones

/*******************************************************************
 WebDSL interprets created/stored Date/DateTime objects as moments in the timezone of the JVM.
 This elib will override the Date(Time) input and output templates, and displays the dates
 in a given timezone. This timezone needs to be returned by a global function `getViewTimeZone(e : Entity)`, which you need to implement.
 The entity `e` passed to `getViewTimeZone(e)` is the owning entity of the Date(Time) property which the input/output template
 is called with. This entity _can_ be used to derive the timezone from, but that depends on the app code. It's recommended to set the
 timezone once per request and return this timezone by `getViewTimeZone(e)` if the app context allows this.
 
 Getting started:
    - Timezones can be identified with their id (String). The template `inputTimeZone( prop : Ref String )` allows the selection of a timezone from a list and stores the timezone id in the given `prop`.
    - `TimeZone.getTimeZone( id )`: Gets the TimeZone object for the given timezone id.
    - `tz.getID()` : get the id from a given TimeZone object tz. 
    - `tz.getDisplayName()`: get the display name of a TimeZone object tz.
    - `output(tz : TimeZone)`: renders the display name of a TimeZone object.
    - `d.toLocalTime(tz)`: get a Date(Time) object projected in the given TimeZone tz from a Date(Time) object d which was based on the default JVM timezone. E.g, to display a persisted DateTime object in a different timezone than the server
    - `d.toServerTime(tz)`: get a Date(Time) object projected in default JVM timezone ( given TimeZone tz from a Date(Time) object d which was based on the default JVM timezone: , i.e. when the value is to be persisted
    - `TimeZoneUtil.getServerTimeZone()`: get the TimeZone object of the server, i.e. the default JVM TimeZone.
    
  Note: No refactoring is needed for cases where the default input and output templates are used. However, cases where date.format("some format") is used, one need to rewrite these to date.toLocalTime( tz ).format("some format").
******************************************************************/

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
  if(d != null){  span[title=timezone.getDisplayName()]{ output( d.toLocalTime(timezone).format( dateformat ) ) } }
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
  output( tz.getDisplayName() )
}

template inputTimeZone( prop : Ref String ){
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