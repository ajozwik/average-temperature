'''
This program reads csv files and merges them based on a `year` column.
'''
# import the pandas library
# you can install using the following command: pip install pandas

import pandas as pd
import glob
import sys
import shutil

dir = sys.argv[1]
city = sys.argv[2]

mask = dir + f"/{city}*.csv"

files = [f for f in glob.glob(mask)]

first = files[0]
rest = files[1:]

dest = f"{dir}/{city}_merged.csv"

shutil.copyfile(first, dest)

def merge(a:str,b:str,c:str):
    year = 'year'
    df1 = pd.read_csv(a)
    df2 = pd.read_csv(b)
    # Merge the two dataframes, using year column as key
    df3 = pd.merge(df1, df2, on = year)
    df3.set_index(year, inplace = True)
    # Write it to a new CSV file
    df3.to_csv(c)
    return c


for file in rest:
    merge(dest,file,dest)

print(f"merged to {dest}")