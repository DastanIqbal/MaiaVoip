Maia VoIP
Android SIP stack based VoIP software.
Linas Martusevicius, "UAB inlusion Netforms" 2014.

Built using Android Studio 0.8.14
Project structure is a slightly tweaked MVC structure, which is subject to change during project handover.
All reusable, selector, and layered drawables are in the res->drawable directory.
Color palette located in res->values->styles.xml

The testing devices were...

	 ___________________________________________________________________________________
	|_DEVICE_______________________|_PLATFORM____________|_DENSITY__|_IMPLEMENTATION____|\
	| Google (Asus) Nexus 7 (2013) | Android 4.2 - 4.4.4 | xhdpi	| most supported	| |
	| LG Nexus 5 				   | Android 4.2 - 5.0   | xxhdpi	| average support	| |
	| Samsung Galaxy Tab 10 (2014) | Android 4			 | xhdpi	| least supported	| |
	|______________________________|_____________________|__________|___________________| |
	\______________________________\_____________________\__________\____________________\|
	
SIP communications testing provider...

	- Linux Mint 16 XFCE 32
	- Kamailio SIP server 
	- Everything (the server, as well as the testing devices) was within a C class network, 
	meaning no implementation was developed for traversing firewalls, routers or mobile networks.
	
Priority features that are yet to be implemented...

	- Accepting calls (firing an onRinging event from the CallCenter class)
	- Log-in functionality (no authentication method has been negotiated with the SIP service provider)
	- History Fragment
	- Persistence
	- Contact list manipulation
	

