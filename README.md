JVS
===

Java Version Switcher
----

The goal of this project is to make easier to use different java versions on windows operating system. [download executable]

#####Features:
* You can change the JAVA_HOME.
* You can open user defined console with different java version.
* You can run tasks. The task is a user defined commands *(for example: "java -jar ...", "fo.bat", etc... ) * on a user defined java version.

#####Hot keys:
* ctrl + shift + j : open context menu
* ctrl + shift + 0 : open console with system java version
* ctrl + shift + #n : open the n. type java console

#####Configuration
* The configuration file is config.xml 
* Syntax of the configuration file:
    * Root node is "config".
	* You have to define <home> nodes as child of <java> node. You have to define the a name and a path for each home.
	* You can redefine the build in windows console something else *(1)*
	* You can define task under java/"name of a home". Each task have to define a name and a command.
* example:

		<config>
			<java>
				<home>
					<name>java7</name>
					<path>C:\Program Files\Java\jdk1.7.0_40</path>
				</home>
			</java>
		
			<console>d:\portable\Console2\Console.exe</console>
		
			<run>
				<java7>  <!-- this node have to same as one of the java/home/name -->
					<task>
						<name>test2</name>
						<command>.\foo.cmd</command>
					</task>
				</java7>
			</run>
		</config>
	


#####Requirements:
* This application work with windows 7 64bit only *(2)*.
* The application suppose that no java on global path. JAVA_HOME is deffined as user environment variable, and the user`s path use it in this way: " %JAVA_HOME%/bin ".
* Java installer put themselves into "c:\Windows\System32". You have to purge them *(3)*. You can test it with "where java" command.
	
1. for example [console2.0] 
2. for 32bit wersion replace JIntellitype.dll with it`s 32 bit version of [JIntellitype]
3. three file: java, javac, javaw

License
----

MIT


**Free Software, Hell Yeah!**

> Comming soon:
> * real error handling
> * better support of different environment
> * standalone version 
>   * with build in jre 
>   * with one click jdk install
> * configurable hotkeys

[console2.0]:http://sourceforge.net/projects/console/
[JIntellitype]:https://code.google.com/p/jintellitype/
[download executable]:https://github.com/PalSzak/jvs/blob/master/executable/jvs-jre7.zip?raw=true