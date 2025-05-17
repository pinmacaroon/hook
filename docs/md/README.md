# dchook

Send Minecraft chat messages to Discord server channels with ease! dchook is a
lightweight and simple mod for the fabric mod loader, and Minecraft versions
`1.20.4` to `1.20.6` (more supported versions coming soon).

![chat message transfer example](https://cdn.modrinth.com/data/qJ9ZfKma/images/0e822ef2aec1062fd27973191cb2cf85a5734910.png)

## Features

This mod offers:

- one way chat message proxying (two way is planned);
- simple configuration;
- straightforward features;
- server-side, Discord Bot-less architecture;

### Configurable and toggleable messages

Toggle server status messages such as: server staring, server started, server
stopping, server stopped.

Toggle wether users can make their messages unproxyable (ignored by the mod) by
applying two forward slashes (`//`) at the end of their chat messages.

### Easy setup

Setting this mod up is just a matter of copying a link and pasting it in!

## Setup

Firstly, you need to generate the config file.

1. Put the mod in your server's `mods` folder;
2. Start your server;
3. Once it started successfully, stop it;

Then, lets set up the mod!

1. Go to the channel you want messages to appear in;
2. Go to it's settings;
3. Go to the `Integrations` menu;
4. Go to the `Webhooks` list;
5. Press `New Webhook` (the blue button). Customization is optional but
   recommended;
   ![webhook menu](https://cdn.modrinth.com/data/cached_images/4213ce48e020a1ca1cb7036dfe85164ca6ba79fd.png)
6. Once you are done with ricing up the webhook, click `Copy Webhook URL` (the
   grey button);
7. Go to your server's `config` folder;
8. Open the file `dchook.properties` in a text editor (Notepad, VI, etc.);
9. Change the value of `webhook.url` to the URL you copied;
10. Start the server again and see the magic unravel!

## "HELP, IT AINT WORKIN', WHAT AM I SUPPOSD' TO DO?"

Calm down, we'll go step by step.

Check the config file.

- Did I paste the URL to the right place?
- Is it the right URL?
- Did I delete an `=`?
- Did I put an extra space somewhere?
- Did I accidentally turn the mod off?

Check the console of the server.

- Did my server say something about the webhook not being a valid API endpoint?
- Did my server say something about the webhook not being found or not having
  connection to Discord?

Still doesn't work? Well, I (pin, the one who made this mod) am also a "human",
so I make mistakes too. If you think I made one, report it to me! See the
sidebar for links!

## Credits

- This mod uses
  [simple-config](https://github.com/magistermaks/fabric-simplelibs/tree/master/simple-config)
  for configuration file management.
- This mod uses [crafthead](https://crafthead.net/)'s API for player chat icons.
