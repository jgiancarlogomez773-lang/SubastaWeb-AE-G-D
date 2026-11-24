# =====================================================================
# T1-18 - Restauracion de un respaldo de SubastaWeb (Windows / PowerShell)
#
# Uso (desde la carpeta del proyecto):
#   powershell -ExecutionPolicy Bypass -File base-de-datos\scripts\restaurar.ps1 -Archivo "RUTA\al\archivo.backup"
#
# Por seguridad restaura en una base NUEVA (por defecto "subastaweb_restaurada"),
# asi nunca se pisa la base original. Si la base destino ya existe, se detiene.
# =====================================================================
param(
    [Parameter(Mandatory = $true)][string]$Archivo,
    [string]$BaseDestino = "subastaweb_restaurada",
    [string]$Usuario     = "postgres",
    [string]$Servidor    = "localhost",
    [int]$Puerto         = 5432
)

$ErrorActionPreference = "Stop"

if (-not (Test-Path $Archivo)) { throw "No existe el archivo: $Archivo" }

function Buscar-Herramienta($nombre) {
    $cmd = Get-Command $nombre -ErrorAction SilentlyContinue
    if ($cmd) { return $cmd.Source }
    $ruta = Get-ChildItem "C:\Program Files\PostgreSQL\*\bin\$nombre.exe" -ErrorAction SilentlyContinue |
            Sort-Object FullName -Descending | Select-Object -First 1 -ExpandProperty FullName
    if (-not $ruta) { throw "No se encontro $nombre. Revisa que PostgreSQL este instalado." }
    return $ruta
}
$psql      = Buscar-Herramienta "psql"
$createdb  = Buscar-Herramienta "createdb"
$pgRestore = Buscar-Herramienta "pg_restore"

$clave = Read-Host "Clave del usuario $Usuario" -AsSecureString
$env:PGPASSWORD = [System.Net.NetworkCredential]::new("", $clave).Password

try {
    $existe = & $psql -h $Servidor -p $Puerto -U $Usuario -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname = '$BaseDestino'"
    if ($existe -eq "1") { throw "La base '$BaseDestino' ya existe. Usa otro nombre con -BaseDestino o borrala primero." }

    Write-Host "Creando base '$BaseDestino' ..."
    & $createdb -h $Servidor -p $Puerto -U $Usuario $BaseDestino
    if ($LASTEXITCODE -ne 0) { throw "createdb fallo (codigo $LASTEXITCODE)." }

    Write-Host "Restaurando $Archivo ..."
    & $pgRestore -h $Servidor -p $Puerto -U $Usuario -d $BaseDestino --no-owner --exit-on-error $Archivo
    if ($LASTEXITCODE -ne 0) { throw "pg_restore fallo (codigo $LASTEXITCODE)." }

    Write-Host "Conteo de filas en la base restaurada:"
    & $psql -h $Servidor -p $Puerto -U $Usuario -d $BaseDestino -c "SELECT 'users' AS tabla, count(*) FROM users UNION ALL SELECT 'categories', count(*) FROM categories UNION ALL SELECT 'auctions', count(*) FROM auctions UNION ALL SELECT 'auction_images', count(*) FROM auction_images;"
    Write-Host "OK: restauracion completa en '$BaseDestino'." -ForegroundColor Green
}
finally {
    Remove-Item Env:\PGPASSWORD -ErrorAction SilentlyContinue
}
