# dchook documentation

## `dchook.properties`

`dchook.properties` is a configuration file generated to and expected from the
`/config/` directory of your server root. if it's not found, it will be
generated using default values.

### syntax

the syntax is very straightforward:

```properties
# [comment]
[key]=[value]
```

a key must be a chunk of text that only has lowercase latin letters with dots
or underscores. a comment can be anything, as its ignored. a value can be a
chunk of text (string), a number or `true`/`false` (boolean). example:

```properties
# start message
# default: The server is starting!
messages.server.starting=The server is starting!

# start message allowed?
# default: true
messages.server.starting.allowed=true
```

### keys

| parameter key | default[1] | fallback[2] | use                                                                                    |
| --- | --- | --- |----------------------------------------------------------------------------------------|
| functions.mod_enabled | true | false | enables/disables the mod's functionality                                               |
| functions.allow_ooc_messages | true | false | allow players to be ignored from proxying if their message ends with double slashes?   |
| functions.promotions.enabled | true | false | are tips and hints/promotion embeds allowed to be sent to Discord                      |
| webhook.url | <https://discord.com/api/webhooks/000/ABCDEF> | (blank string) | url of webhook                                                                         |
| messages.server.starting | The server is starting! | messages.server.starting | start message                                                                          |
| messages.server.stopped | The server has been stopped! | messages.server.stopped | stop message                                                                           |
| messages.server.started | The server has started! | messages.server.started | opened/fully started message                                                           |
| messages.server.stopping | The server is stopping! | messages.server.stopping | stopping message                                                                       |
| messages.server.starting.allowed | true | false | start message allowed?                                                                 |
| messages.server.stopped.allowed | true | false | stop message allowed?                                                                  |
| messages.server.started.allowed | true | false | opened/fully started message                                                           |
| messages.server.stopping.allowed | true | false | stopping message allowed?                                                              |
| messages.server.game.allowed | true | false | default leave/join, advancement and death messages allowed? (currently not functional) |

1. default, as in it's the value generated with the file
2. fallback, as in if the key isn't found, this value will be used instead

## features

### ooc messages

out-of-character messages. ending a message with double slashes (`//`) will
tell the mod to ignore your message. this can be turned on/off.

### (TO BE ADDED) two-way hook

send a message in the desired Discord channel, and make that message appear in
game!
