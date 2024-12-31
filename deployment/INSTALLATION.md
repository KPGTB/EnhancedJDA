# Discord bot installer creator

## Windows

**Requirements:**

-   Java Development Kit (at least 14, but not lower than used by bot)
-   [WiX Toolset](https://github.com/wixtoolset/wix3/releases)

**Run `windows_installer.bat` and follow instructions**

### Installation

Run installator and install app. After than run the bot using installed .exe

### Configuration

Configuration files will be located in directory which you chose during installation. **Make sure to configure .env**

## Linux

**Requirements:**

-   Java Development Kit (at least 14, but not lower than used by bot)

**Run `linux_installer.sh` and follow instructions**

### Installation

1. Run `dpkg -i <output_file>.deb` (`<output_file>` is a name of created installer)
2. If you don't have some dependencies then run `apt -f install` and run command from 1st step again

Package will be installed in `/opt/<application-name>`. You can run it using `/opt/<formatted-application-name>/bin/<application-name>`. `formatted-application-name` is a lower case name with `-` instead of spaces

### Running bot in background

1. Create service file `nano /etc/systemd/system/<application-name>.service`

```
[Unit]
Description=Discord bot

[Service]
User=root
Group=root
Restart=always
WorkingDirectory=/opt/<formatted-application-name>
ExecStart=/opt/<formatted-application-name>/bin/<application-name>
StartLimitInterval=180
StartLimitBurst=30
RestartSec=5s

[Install]
WantedBy=multi-user.target
```

2. Enable service `systemctl enable <application-name>`
3. Start service `systemctl start <application-name>`

To check logs use `journalctl -fu <application-name>`

### Configuration

Configuration files will be located in `/opt/<formatted-application-name>`. **Make sure to configure .env**

## Pterodactyl

Run JAR using [Generic Java Egg](https://github.com/pelican-eggs/eggs/blob/master/generic/java/egg-java.json)

## Docker

Use Dockerfile with JAR file in the same directory. Add storage for `.env`, `./logs`, `database.db` (if sqlite is used) and other data files
