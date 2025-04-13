# Pantheon
A simple Towny Addon that lets residents select a religion. 

## Installation
1. Have Towny Installed on your Server
2. Install plugin in your plugins folder.
3. Start the Server

## How to Manage
1. in `plugins/Pantheon`, edit the `religion.yml` to add new religions.
2. Run `/pantheon reload religions` in console or ingame to reload and add any new religions
3. Running `/pantheon reload religions` will also clear the cache to fix any calculation issues.

## How to use it
1. A resident will be able to type `/res set religion <religion>` with automatic suggestions of everything in religion.yml
2. Once a resident sets their religion, their town and nation religion data will refresh. When you type `/t` or `/n`, the Status Screen will show "`Religion breakdown: Catholic 7%, Unknown 93%`"
3. As more residents select a specific religion, the Status Screen will reflect the changes

### Todo
- Create a Chat Channel for each Religion
- Add an automatic updater or update reminder
