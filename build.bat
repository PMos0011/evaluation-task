FOR %%s IN (credit customer product) DO (
    cd %%s
    call mvn clean install
    call docker build -t %%s-img .
    cd ..
)
pause