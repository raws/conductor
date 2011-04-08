Conductor is a [Minecart Mania](http://wiki.afforess.com/Minecart_Mania-Plugin) plugin to help you disembark at the right stop.

Conductor is inspired by [DjDCH](http://forums.bukkit.org/members/djdch.1819/)'s [NextStation](http://forums.bukkit.org/threads/mech-nextstation-0-1-1-your-favourite-stations-announcement-system-556.6572/). I wanted the ability to debark at _any_ upcoming station, and to use natural-reading signs to do so.

### Installation and usage

Conductor requires [Minecart Mania](http://wiki.afforess.com/Minecart_Mania-Plugin) Core. Simply drop `MinecartManiaConductor.jar` into your [Bukkit](http://bukkit.org/) server's `plugins` directory.

Conductor exposes one simple command. To set your minecart destination, use:

    /to <destination>

Where `<destination>` is the name of the station you'd like to debark at. Station names may be partial, and are case insensitive. For example, `/to gaf` will match the station "Fort Gafley".

To clear your destination, use `/to` without any parameters.

To create a station, place rail on top of a log block. Adjacent to the log (within a two-block radius in all directions), place a sign which looks like:

     -----------------
    |                 |
    |  Wheaties Line  |
    |   Fort Gafley   |
    |                 |
     -----------------
           |  |

Any signs with a "line" identifier (`/.*line/i`) will be matched against your destination. If a match is achieved, your minecart will be stopped, you will be ejected, and the minecart returned to its owner. Otherwise, you will continue rolling on down the line!

### License <small>(MIT)</small>

<small>Copyright © 2011 Ross Paffett.</small>

<small>Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:</small>

<small>The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.</small>

<small>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.</small>
