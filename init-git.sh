#!/bin/bash

echo "# 5200" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/moksaiho/5200.git
git push -u origin main

