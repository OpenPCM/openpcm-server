# Contributing to OpenPCM

I am very excited you have chosen to contrbute to this project. I definitely want the help of other developers to bring about new ideals or complete the current outstanding tasks that we have. 

Here are the current tools we use to manage this project. Some of these tools may change over time and links will be placed here.
- [OpenPCMSlack] is our communication platform
- [Our project plan] is currently hosted here on GitHub Projects
- Report bugs in the [issues] section of this GitHub. A better place would be the #bugs channel on Slack
- Development Information can be found in pages on our [Wiki] hosted here on GitHub

# Testing

## Testing
We currently using [JUnit] tests for our backend services. Please do include unit tests in your new code. We use Sonar as our code analyzer and the link will be publicly available shortly. You feature branch must pass the quality gate before a pull request will be merged.

# Commiting Your Code

Please send a [pull request] once your feature branch is complete. Currently there is no pull request template but that will be added soon. Please follow the coding conventions below and would be nice if all your commits are atomic (one feature per commit). 

Log messages are verry important to us. This is the best way to communicate the intended functionality of your code. Take pride in your commit messages even if others do not :). For very small changes, one liners are definitely okay but if your changeset is large please use the following format:

```
$ git commit -m "A brief summary of the commit
> 
> A paragraph describing what changed and its impact."
```

# Coding Conventions

- Here is the [style guide] for formatting your code. This can be imported into Eclipse of Intellij. If you use a different IDE, please let us know via [OpenPCMSlack] and I'm sure we'll be able to find a resolution.
- This is open source software. Consider the people who will read your code, and make it look nice for them. It's sort of like driving a car: Perhaps you love doing donuts when you're alone, but with passengers the goal is to make the ride as smooth as possible.

# Thank You
We deeply appreciate your help with this project. Hopefully this guide covers everything you need to know. You can always ask questions via [OpenPCMSlack] if unsure!

Thanks, Raymond King

[OpenPCMSlack]: https://openpcm.slack.com
[Our project plan]: https://github.com/OpenPCM/openpcm-server/projects
[issues]: https://github.com/OpenPCM/openpcm-server/issues
[Wiki]: https://github.com/OpenPCM/openpcm-server/wiki
[JUnit]: https://junit.org/junit4/
[pull request]: https://help.github.com/articles/about-pull-requests/
[style guide]: https://drive.google.com/open?id=1qdNh3mmwRtuUPEz6WeFs4S69ryRxCHmJ
