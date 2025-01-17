#!/bin/bash

#export JAVA_HOME=/Users/dmitry/Library/Java/JavaVirtualMachines/temurin-22.0.2/Contents/Home
cd src || exit

javac home/work/links/Config.java \
  home/work/links/data/ShortLink.java \
  home/work/links/services/ShortLinkRepository.java \
  home/work/links/services/ShortLinkService.java \
  home/work/links/services/UserRepository.java \
  home/work/links/services/UserService.java \
  home/work/links/repo/ShortLinkRepositoryImpl.java \
  home/work/links/repo/UserRepositoryImpl.java \
  home/work/Main.java -d ../out