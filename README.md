# uisce_beatha.clj
_Uisce beatha_, or "water of life" in Irish Gaelic.


## About Project
This project aims to provide a convenient way to extract information from
[Oregon's Liquor Search](http://oregonliquorsearch.com).

It's written using [Babashka](https://babashka.org/) and designed
to be a quick, lightweight script that can run on any machine with it installed.


## Usage

### Help
* `bb ./uisce_beatha.clj help` - Get a list of CLI options

### Search

#### Categories
* `bb ./uisce_beatha.clj search -c` - Get a list of possible categories

* `bb ./uisce_beatha.clj search -c "irish"` - Search an individual category

* `bb ./uisce_beatha.clj search -c "irish, scotch, rum"` - Search multiple categories
