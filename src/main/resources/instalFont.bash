sudo mkdir "/usr/share/fonts/truetype"
sudo cp *.ttf "/usr/share/fonts/truetype"
sudo fc-cache -f -v
sudo -k
