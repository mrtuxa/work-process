import dimscord, asyncdispatch, times, options

let discord = newDisccordClient("<token>")

# handle event for message_create
proc messageCreate(s: Shard, m: Message) {.event(discord).} = 
if m.author.bot: return
if m.content == "!ping":
  let
    before = e