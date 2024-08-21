
from bs4 import BeautifulSoup
import requests
import time

print('put some skills that you are not familiar with')
unfamiliar_skill = input('>')
print(f'Filtering out {unfamiliar_skill} ')
def find_jobs():
    r = requests.get('https://www.timesjobs.com/candidate/job-search.html?searchType=personalizedSearch&from=submit&txtKeywords=Web+Application+Developer&txtLocation=').text
    soup = BeautifulSoup(r,'lxml')
    jobs = soup.find_all('li', class_ = 'clearfix job-bx wht-shd-bx')
    for index, job in enumerate(jobs):
         
        published_date = job.find('span',class_= 'sim-posted').span.text
        if 'few' in published_date:
            
            company_name=job.find('h3', class_ = 'joblist-comp-name').text.replace(' ','')
            skills = job.find('span', class_ = 'srp-skills').text.replace(' ','')
            more_info = job.header.h2.a['href']
            if unfamiliar_skill not in skills:
                with open(f'posts/{index}.txt','w') as f :
                    f.write(f"Company Name: {company_name.strip()} \n")
                    f.write(f"Requried skills: {skills.strip()}\n")
                    f.write(f'More Info: {more_info}\n')
                    f.write(f'Published date:{published_date} \n')
                print(f'File saved: { index} ')    
                                  

while True:
        find_jobs()
        time_wait = 10
        print(f'waiting {time_wait} minutes...')
        time.sleep(time_wait * 60)
