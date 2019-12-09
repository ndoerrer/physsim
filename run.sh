#!/bin/sh

if ant; then
	cd build/
	java physsim.exec.TestPhyssim
	cd ..
fi
