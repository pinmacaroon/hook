# dchook documentation

## Table of contents

1. [Table of contents](#table-of-contents)
2. [Legal](#legal)
3. [`dchook.properties`](#dchookproperties)
   1. [Syntax](#syntax)
   2. [Keys](#keys)
4. [Features](#features)
   1. [OOC Messages](#ooc-messages)
   2. [Two-Way Hook](#two-way-hook)
   3. [Xaero's World Map support](#xaeros-world-map-support)
5. [Setup](#setup)
   1. [One-Way Mode](#one-way-mode)
   2. [Two-Way Mode](#two-way-mode)
6. ["HELP, IT AINT WORKIN', WHAT AM I SUPPOSD' TO DO?"](#help-it-aint-workin-what-am-i-supposd-to-do)

## Legal

The software itself is licensed under the MIT license. Dependencies' licence may vary. Please read the Terms of Service before using the software!

## `dchook.properties`

`dchook.properties` is a configuration file generated to and expected from the `/config/` directory of your server root. If it's not found, it will be generated using default values.

### Syntax

The syntax is very straightforward:

```properties
# [comment]
[key]=[value]
```

A key must be a chunk of text that only has lowercase latin letters with dots or underscores. A comment can be anything, as its ignored. A value can be a chunk of text (string), a number or `true`/`false` (boolean). Example:

```properties
# start message
# default: The server is starting!
messages.server.starting=The server is starting!

# start message allowed?
# default: true
messages.server.starting.allowed=true
```

### Keys

| parameter key                    | default[1]                                    | fallback[2]              | use                                                                                    |
|----------------------------------|-----------------------------------------------|--------------------------|----------------------------------------------------------------------------------------|
| functions.mod_enabled            | true                                          | false                    | enables/disables the mod's functionality                                               |
| functions.allow_ooc_messages     | true                                          | false                    | allow players to be ignored from proxying if their message ends with double slashes?   |
| functions.promotions.enabled     | true                                          | false                    | are tips and hints/promotion embeds allowed to be sent to Discord                      |
| functions.bot.enabled            | true                                          | false                    | is two-way chat (the bot) enabled?                                                     |
| functions.bot.token              | "TOKEN"                                       | (blank string)           | bot token                                                                              |
| functions.bot.prefix             | $                                             | $                        | bot command prefix                                                                     |
| functions.update                 | true                                          | false                    | auto check for updates                                                                 |
| webhook.url                      | <https://discord.com/api/webhooks/000/ABCDEF> | (blank string)           | url of webhook                                                                         |
| messages.server.starting         | The server is starting!                       | messages.server.starting | start message                                                                          |
| messages.server.stopped          | The server has been stopped!                  | messages.server.stopped  | stop message                                                                           |
| messages.server.started          | The server has started!                       | messages.server.started  | opened/fully started message                                                           |
| messages.server.stopping         | The server is stopping!                       | messages.server.stopping | stopping message                                                                       |
| messages.server.starting.allowed | true                                          | false                    | start message allowed?                                                                 |
| messages.server.stopped.allowed  | true                                          | false                    | stop message allowed?                                                                  |
| messages.server.started.allowed  | true                                          | false                    | opened/fully started message                                                           |
| messages.server.stopping.allowed | true                                          | false                    | stopping message allowed?                                                              |
| messages.server.game.allowed     | true                                          | false                    | default leave/join, advancement and death messages allowed? (currently not functional) |

1. default, as in it's the value generated with the file
2. fallback, as in if the key isn't found, this value will be used instead

## Features

### OOC Messages

Out-of-Character messages. Ending a message with double slashes (`//`) will tell the mod to ignore your message. This can be turned on/off.

### Two-Way Hook

Send a message in the desired Discord channel, and make that message appear in game!

### Xaero's World Map support

[Xaero's World Map](https://modrinth.com/mod/xaeros-world-map) has a feature, with which you can share waypoints in chat for everyone to save and add. This will be converted to readable coordinates and dimension data when sent to Discord!

## Setup

There are two "modes" in which the mod can operate: one-way and two-way. The former is simpler to set up and less resource heavy, while the latter one is "cooler".

### One-Way Mode

Setting up the one-way chat can be done by following these steps:

Create a Discord webhook:

1. Go to the desired channel you will use as the bridge;
2. Go to the channels settings;
3. Go to the *Integrations* tab;
4. Go to the *Webhooks* menu item;
5. Click the blue *New Webhook* button;
6. Click on your new webhook and press the *Copy Webhook URL*.

Set the mod up:

1. Put the mod's jar in your server's `/mods` folder;
2. Start the server;
3. Once it has fully started, shut it down;
4. Open the configuration file in `/config/dchook.properties`;
5. Change the value of `webhook.url` to your newly copied webhook URL;
6. Save the configuration file;
7. Start the server again and see the magic happen!

### Two-Way Mode

Setting up the two-way chat can be done by following these steps, but be aware, this needs quite much technical knowledge. This also requires you to accept Discord's TOS and Developer TOS! Also, please set up the one way chat first!

Create a Discord Application:

1. Go to the [Discord Developer Portal](https://discord.com/developers/applications) and log in if needed;
2. Click on the *Applications* sidebar item;
3. Click on *New Application*;
4. In the dialog, give your app a name, and after reading the licenses and terms, click on the checkbox and press *Create*;
5. Once created, go to the sidebar's *Bot* menu item;
6. On the *Bot* page, under *Privileged Gateway Intents*, find *Message Content Intent* and turn it on;
7. Under *Privileged Gateway Intents*, find *Server Members Intent* and turn it on too;
8. On the *Bot* page, click on the blue button labeled *Reset Token*, and authenticate yourself;
9. In the buttons place should be a long token, copy the whole thing;
10. Go to the *Installation* sidebar menu item;
11. Under *Default Install Settings*: *Guild Install* select a new scope called `bot`, then add the following permissions to your bot:
    * *Attach Files*;
    * *Manage Webhooks*;
    * *Read Message History*;
    * *Send Messages*;
    * *Send Messages in Threads*.
12. A strip should appear prompting you to save your changes, so click on *Save Changes*;
13. On the same page, under *Install Link*, open the Discord Provided Link in a new browser tab, and add the bot to your server;
14. If the bot successfully joins your server, then you should proceed to the next steps!

Set the mod up:

1. Open the configuration file in `/config/dchook.properties`;
2. Change the value of `functions.bot.token` to your bot's token you copied in step 9 of the previous set of steps;
3. Save the configuration file;
4. Start the server again and type something in game and in your channel too, and see if your messages appear!

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
so I make mistakes too. If you think I made one, report it to me!