WebDSL interprets created/stored Date/DateTime objects as moments in the timezone of the JVM. This elib will override the Date(Time) input and output templates, and displays the dates
in a given timezone.

This timezone needs to be returned by a global function `getViewTimeZone(e : Entity)`, which you need to implement.
The entity `e` passed to `getViewTimeZone(e)` is the owning entity of the Date(Time) property which the input/output template
is called with. This entity _can_ be used to derive the timezone from, but that depends on the app code. It's
recommended to set the timezone once per request and return this timezone by `getViewTimeZone(e)` if the app context allows this.
 
Getting started:

- Timezones can be identified with their id (String). The template `inputTimeZone( prop : Ref String )` allows the selection of a timezone from a list and stores the timezone id in the given `prop`.
- `TimeZone.getTimeZone( id )`: Gets the TimeZone object for the given timezone id.
- `tz.getID()` : get the id from a given TimeZone object tz. 
- `tz.getDisplayName()`: get the display name of a TimeZone object tz.
- `output(tz : TimeZone)`: renders the display name of a TimeZone object.
- `d.toLocalTime(tz)`: get a Date(Time) object projected in the given TimeZone tz from a Date(Time) object d which was based on the default JVM timezone. E.g, to display a persisted DateTime object in a different timezone than the server
- `d.toServerTime(tz)`: get a Date(Time) object projected in default JVM timezone ( given TimeZone tz from a Date(Time) object d which was based on the default JVM timezone: , i.e. when the value is to be persisted
- `TimeZoneUtil.getServerTimeZone()`: get the TimeZone object of the server, i.e. the default JVM TimeZone.
    
 __Note__: No refactoring is needed for cases where the default input and output templates are used. However, cases where `date.format("some format")` is used, one need to rewrite these to `date.toLocalTime( tz ).format("some format")`.
