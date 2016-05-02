# DempsterShafer

This repo is an extension of the research I've been completing during my senior year Cognitive & Brain Science Seminar at Tufts University. This research focuses on Dempster-Shafer's Evidence Theory and the different similarity measures used to compare two separate bodies of evidence (BoE's). I've implemented this java library for further use in the research and development of embodied cognitive systems. In order to compile these files I've provided, you'll need to have the latest version of Apache Ant installed. If you're using a Linux machine, open a terminal window and type the following in the command line: "sudo apt-get install ant" and then follow the instructions. If you're using Unix/Windows, download a binary distribution of ANT [here](http://ant.apache.org/bindownload.cgi), then choose a mirror and download the zip (zip is the easiest method to set). Unzip the file on your computer, then copy the path (traverse until the bin folder). Next, you'll need to set up an environment variable. In your bash_profile, Set ANT_HOME = installation path (the path you've just copied) and save. Now execute, echo $ANT_HOME and $JAVA_HOME respectively. You should see the result as your installation path. Before compiling any of the provided source code, you'll need to change your anthome environment in your bash script. 
Include the following in your bash_profile (UNIX) or follow the instructions from this [link](https://docs.oracle.com/cd/E19316-01/820-7054/gicjc/index.html) to change anthome: 

export ANT_HOME=/opt/apache-ant-1.9.7
export PATH=$ANT_HOME/bin:$PATH

Once ANT is installed and you've changed your Ant Home environment your work is done.

Run the command ./ant dsjar to get a JAR of all the source code included. 
