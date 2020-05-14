module elib/elib-timezones/timezone-override-templates

section overridden templates

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

override template datepickerinput( d: ref Date, internalJavaDateFormat : String, visibleJavaDateFormat: String, tname: String, options: String ){
  var s: String        
  var defaultFlatPickrFormat := convertJavaDateFormatToFlatPickr( internalJavaDateFormat )
  var req := getRequestParameter( tname )
  var flatPickrAltDateFormat := convertJavaDateFormatToFlatPickr( visibleJavaDateFormat )
  var onOpen := "onOpen: function(dateObj, dateStr, instance){ if(dateStr == ''){ instance.jumpToDate( new Date() ); } }"
  var timezone : TimeZone
  init{
    if( d == null ){
      s := "";
      timezone := getViewTimeZone(null);
    }
    else{
      timezone := getViewTimeZone(d.getEntity());
      s := d.toLocalTime(timezone).format( internalJavaDateFormat );
      
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
    $("input:not(.flatpickr-input)[name=~tname]").flatpickr({~onOpen, allowInput: true, dateformat: '~defaultFlatPickrFormat', altFormat: '~flatPickrAltDateFormat' , altInput: true, time_24hr: true, ~options});
  </script>

  databind{
    if( req != null ){
      if( req == "" ){
        d := null;
      }
      else{
        var newdate := req.parseDateTime( internalJavaDateFormat );
        if( newdate != null ){
          newdate := newdate.toServerTime(timezone);
          //first compare dates on Unix epoch time before changing d. Prevents triggering a change when timezone information is different or added, while timestamp is the same 
          if( d == null || d.getTime() != newdate.getTime()){
            d := newdate;
          }
        }
      }
    }
  }
}