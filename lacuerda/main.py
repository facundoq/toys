import requests
import os,sys
from bs4 import BeautifulSoup
import random
from time import sleep



def make_html(title,body):
    template = f"""
    <!doctype html>

<html lang="es">
<head>
  <meta charset="utf-8">
  <title>{title}</title>
    <style>
        a{{
        color:blue;
        font-weight:bold;
        }}
    </style>  
</head>

<body>
  {body}
</body>
</html>
"""
    return template
def parse_name_author(title):
    name_author = title.split(",")
    if len(name_author) == 0:
        print("Warn: no name/author found")
        name= "Unknown"
        author = "Unknown"
    elif len(name_author) == 1:
        print(f"Warn: single name/author: {name_author}")
        name=author=name_author[0]
    elif len(name_author) == 2:
        name,author=name_author
    else:
        print(f"Warn: multiple name/authors: {name_author}")
        name,author=",".join(name_author[0:-1]),name_author[-1]
    author = author.strip().title()
    name = name.strip().title()
    return name,author

def get_song(url):
    page = requests.get(url)
    soup = BeautifulSoup(page.content, 'html.parser')
    title = soup.find_all('title', limit=1)[0].text
    title = title.split(":")[0]
    name,author = parse_name_author(title)

    title = f"{author} - {name}"
    song = str(soup.find(id='t_body'))
    return title,song

def urls_songbook(filepath:str,sleep_min=0.1,sleep_max=2):
    if not os.path.exists(filepath):
        sys.exit(f"Non-existent path {filepath}")
    name = os.path.basename(filepath)
    name = os.path.splitext(name)[0]
    urls = open(filepath).readlines()
    urls = [url.strip() for url in urls]

    songs = []
    for url in urls:
        print(f"Retrieving {url}...")
        title, song = get_song(url)
        sleep(random.uniform(sleep_min,sleep_max))
        song_body = f"<h2>{title}</h2>\n{song}"
        songs.append(song_body)
    with  open(f"{name}.html", "w+") as file:
        body = "<hr><br>".join(songs)
        file.write(make_html(name, body))


if __name__ == '__main__':
    urls_songbook("Silvio.txt")