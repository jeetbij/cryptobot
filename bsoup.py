import requests
import json
from bs4 import BeautifulSoup


# Base url to scrap
BASE_URL = 'https://goldprice.org/cryptocurrency-price'
# headers to pass, hardcoded for now
HEADERS = {
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36',
        'Content-Type': 'application/json'
    }

CRYPTO_BOT_SERVER = "http://35.188.47.229:8000"

def scrap_crypto_price(url, headers):
    '''
    Accepts - url, headers
    Returns - List of Cryptocurrency hash (contains name, price and change24h)
    '''
    page = requests.get(url, headers=headers)

    soup = BeautifulSoup(page.content, 'html.parser')

    div = soup.find('div', class_='pane-cryptocurrency-index-panel-pane-1')

    table = div.find('table', class_='table-0')

    tbody = table.find('tbody')

    trs = tbody.find_all('tr')

    crypto_list = []

    dollar_value = get_dollar_value_in_rupees(headers)

    for tr in trs:
        # Getting cryptocurrency name, price and change24h
        name_tag = tr.find('td', class_='views-field-field-crypto-proper-name')
        price_tag = tr.find('td', class_='views-field-field-crypto-price')
        change_tag = tr.find('td', class_='views-field-field-crypto-price-change-pc-24h')

        if None not in (name_tag, price_tag, change_tag):
            name_text = name_tag.text.strip()
            price_text = price_tag.text.strip().replace('$', '').replace(',', '')
            change_text = change_tag.text.strip().replace('%', '')

            if None not in (name_text, price_text, change_text):
                crypto_list.append(
                    {
                        "name": name_text,
                        "price": float(price_text) * float(dollar_value),
                        "change24h": float(change_text),
                    }
                )
    
    return crypto_list

def get_dollar_value_in_rupees(headers):

    WEB_URL = "https://in.investing.com/currencies/usd-inr"

    page = requests.get(WEB_URL, headers=headers)

    soup = BeautifulSoup(page.content, 'html.parser')

    div = soup.find('div', class_='last-price-and-wildcard')

    dollar_value = div.find('bdo').text.strip()

    return dollar_value



def send_crypto_updates_to_server():
    
    ccs = scrap_crypto_price(BASE_URL, HEADERS)

    for cc in ccs:
        cc["currency"] = "INR"
        print(cc)
        res = requests.post(CRYPTO_BOT_SERVER + "/create", headers=HEADERS, data=json.dumps(cc))
        print(res.text)

print(send_crypto_updates_to_server())
