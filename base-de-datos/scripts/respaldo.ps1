# =====================================================================
# T1-18 - Respaldo de la base de datos de SubastaWeb (Windows / PowerShell)
#
# Uso (desde la carpeta del proyecto):
#   powershell -ExecutionPolicy Bypass -File base-de-datos\scripts\respaldo.ps1
#   powershell -ExecutionPolicy Bypass -File base-de-datos\scripts\respaldo.ps1 -BaseDatos subastaweb
#
# Genera un archivo .backup (formato custom de pg_dump) en la carpeta
# Documentos\SubastaWeb_respaldos. Los respaldos NO se suben a Git.
# =====================================================================
param(
    [string]$BaseDatos = "subastaweb_prueba",
    [string]$Usuario   = "postgres",
    [string]$Servidor  = "localhost",
    [int]$Puerto       = 5432,
    [string]$Carpeta   = "$HOME\Documents\SubastaWeb_respaldos"
)

$ErrorActionPreference = "Stop"

# Buscar pg_dump (instalador oficial de PostgreSQL para Windows)
$pgDump = Get-Command pg_dump -ErrorAction SilentlyContinue
if ($pgDump) { $pgDump = $pgDump.Source }
else {
    $pgDump = Get-ChildItem "C:\Program Files\PostgreSQL\*\bin\pg_dump.exe" -ErrorAction SilentlyContinue |
              Sort-Object FullName -Descending | Select-Object -First 1 -ExpandProperty FullName
}
if (-not $pgDump) { throw "No se encontro pg_dump. Revisa que PostgreSQL este instalado." }

New-Item -ItemType Directory -Force -Path $Carpeta | Out-Null
$fecha   = Get-Date -Format "yyyyMMdd_HHmmss"
$archivo = Join-Path $Carpeta "$BaseDatos`_$fecha.backup"

# La clave se pide en pantalla y no se guarda en ningun archivo
$clave = Read-Host "Clave del usuario $Usuario" -AsSecureString
$env:PGPASSWORD = [System.Net.NetworkCredential]::new("", $clave).Password

try {
    Write-Host "Respaldando '$BaseDatos' en $archivo ..."
    & $pgDump -h $Servidor -p $Puerto -U $Usuario -F c -f $archivo $BaseDatos
    if ($LASTEXITCODE -ne 0) { throw "pg_dump fallo (codigo $LASTEXITCODE)." }
    $tamano = [math]::Round((Get-Item $archivo).Length / 1KB, 1)
    Write-Host "OK: respaldo creado ($tamano KB): $archivo" -ForegroundColor Green
}
finally {
    Remove-Item Env:\PGPASSWORD -ErrorAction SilentlyContinue
}
