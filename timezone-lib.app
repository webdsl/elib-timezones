module elib/elib-timezones/timezone-lib

section Date/DateTime extensions

type DateTime{
  //the timezone argument is the local timezone in both cases
  org.webdsl.utils.TimeZoneUtil.toLocalTime as toLocalTime(TimeZone) : DateTime
  org.webdsl.utils.TimeZoneUtil.toServerTime as toServerTime(TimeZone) : DateTime
  org.webdsl.utils.TimeZoneUtil.toUTCTime as toUTCTime(TimeZone) : DateTime
  org.webdsl.utils.TimeZoneUtil.transformBetweenTimeZones as toTimeZone(TimeZone, TimeZone) : DateTime
}

native class java.util.TimeZone as TimeZone{
  // getDisplayName() : String
  getID() : String
  static getTimeZone(String) : TimeZone
  getOffset(Long) : Int
  org.webdsl.utils.TimeZoneUtil.displayName as getDisplayName() : String
  org.webdsl.utils.TimeZoneUtil.fullDisplayName as getFullDisplayName() : String
  org.webdsl.utils.TimeZoneUtil.offsetRelativeToServer as getOffsetRelativeToServer(Date) : Int
}

native class org.webdsl.utils.TimeZoneUtil as TimeZoneUtil{
  static toServerTime(Date, TimeZone) : Date
  static toLocalTime(Date, TimeZone) : Date
  static timeZoneIds() : List<String>
  static timeZoneLabels() : List<String>
  static timeZoneOffsetMinutes() : List<Int>
  static getServerTimeZone() : TimeZone
  static offsetMillisToTimeZone(Date, Int) : TimeZone
}

function serverTimeZone() : TimeZone { return TimeZoneUtil.getServerTimeZone(); }
function getAoETimeZone() : TimeZone { return TimeZone.getTimeZone("Etc/GMT+12"); }

section templates

template output(tz : TimeZone){
  span[class="timezone-info"]{ " " output( tz.getDisplayName() ) }
}

template inputTimeZone( prop : ref String){
	inputTimeZone(prop, false)[all attributes]
}
template inputTimeZone( prop : ref String, autoSelectClientTZ : Bool ){
  var fromValues := TimeZoneUtil.timeZoneIds()
  var fromLabels := TimeZoneUtil.timeZoneLabels()
  var tname := getTemplate().getUniqueId()
  var req := getRequestParameter(tname)
  var shouldAutoSelect := autoSelectClientTZ && (prop == null || prop == "")
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
            
            if(shouldAutoSelect){
              data-utc-minute-offset=TimeZoneUtil.timeZoneOffsetMinutes()[idx]
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
  
  if(shouldAutoSelect){
  	<script>
  	  var tzOffsetMin = new Date().getTimezoneOffset()*-1;
			var sel = document.getElementsByName('~tname')[0]
			var opts = sel.options;
			for (var opt, j = 0; opt = opts[j]; j++) {
			  if (opt.getAttribute('data-utc-minute-offset') == tzOffsetMin) {
			    sel.selectedIndex = j;
			    break;
			  }
			}
  	</script>
  }
  
}