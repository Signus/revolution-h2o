#! /usr/bin/perl -w
#------------------------------------------------------------------------------
# Author : Chris Card
# Date: 5/30/13
# Description: this is the level generator for cleanwatergame it only generates
# the ground
#
# usage: LevelGenerator.pl <LevelName> <path> <width> <height>
#                -<LevelName> = name of the level i.e. act1scene1
#				 -<path> = The directory to save the file to
#				 -<width> = widht of the level
#				 -<height> = height of the level
#
#------------------------------------------------------------------------------

use strict;

#------------------------------------------------------------------------------
# Variables:
#------------------------------------------------------------------------------
my $entitySt = "	<entity x=\"";
my $endentity = "\" y=\"25\" width=\"100\" height=\"50\" type=\"ground\"/>\n";
my $width = "";
my $height = "";
my $path = "";
my $levelname = "";
my $xmlVer = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
my $levelb = "<level width=\"";
my $levelh = "\" height=\"";
my $levelbe = "\">\n";
my $levele = "</level>";



#------------------------------------------------------------------------------
# Check user input:
#------------------------------------------------------------------------------
my $numvars = scalar(@ARGV);

if($numvars == 4)
{
	$levelname = $ARGV[0];
	$path = $ARGV[1];
	$width = $ARGV[2];
	$height = $ARGV[3];

	if (-d $path) {
		$path = $path.$levelname."\.xml";
	} else {
		print("Cannot find the directory");
		exit(0);
	}
}
else
{
	showHelp();
	exit(0);
}


#------------------------------------------------------------------------------
# Main:
#------------------------------------------------------------------------------

writeXml();


exit(0);

#-------------------End main---------------------------------------------------

#------------------------------------------------------------------------------
# Functions:
#------------------------------------------------------------------------------

#------------------------------------------------------------------------------
# This writes the xml file
sub writeXml
{
	open(XML, ">$path") or die "Faild to open $path\n\n";

	my $leveltemp = $levelb.$width.$levelh.$height.$levelbe;

	print XML $xmlVer;
	print XML $leveltemp;

	my $entityl = "";

	for (my $i = 50; $i < $width; $i = $i + 100) {
		$entityl = $entitySt."$i".$endentity;
		print XML $entityl;
	}

	print XML $levele;
	close(XML);
}

#------------------------------------------------------------------------------
#this shows the help
sub showHelp
{
	print("LevelGenerator.pl <LevelName> <path> <width> <height>\n");
    print("           -<LevelName> = name of the level i.e. act1scene1\n");
	print(" 		  -<path> = The directory to save the file to\n");
	print("	          -<width> = widht of the level\n");
	print("			  -<height> = height of the level\n\n");
}