Testing Section

My testing theory:
 -- i wrote down all the tests i could think could work
 -- wrote the pseudocode for all of them
 -- implemented what i could; when i forgot the syntax or i got an error, used ai to correct myself

I used slice tests so i could ensure the i could test the controller and repository independently
This ensureed that my tests could be done quicker and are less complex


Tests are run by pasting this command in the terminal: .\mvnw.cmd test

If you get an error, use this command: Remove-Item -Recurse -Force .\target
(for some reason you sometimes have to delete the target file), then run the other command again