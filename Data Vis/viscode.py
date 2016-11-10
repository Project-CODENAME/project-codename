import matplotlib.pyplot as plt
import plotly
import plotly.tools as tls
import pandas as pd
import numpy as np

# Read the data into a pandas DataFrame.
data = pd.read_csv(
    "data.csv", usecols=[0, 4])

# These are the "Tableau 20" colors as RGB.
tableau20 = [(31, 119, 180), (174, 199, 232), (255, 127, 14), (255, 187, 120),
             (44, 160, 44), (152, 223, 138), (214, 39, 40), (255, 152, 150),
             (148, 103, 189), (197, 176, 213), (140, 86, 75), (196, 156, 148),
             (227, 119, 194), (247, 182, 210), (127, 127, 127), (199, 199, 199),
             (188, 189, 34), (219, 219, 141), (23, 190, 207), (158, 218, 229)]

# Scale the RGB values to the [0, 1] range, which is the format matplotlib accepts.
for i in range(len(tableau20)):
    r, g, b = tableau20[i]
    tableau20[i] = (r / 255., g / 255., b / 255.)

# You typically want your plot to be ~1.33x wider than tall.
# Common sizes: (10, 7.5) and (12, 9)
plt.figure(figsize=(12, 9))

# Remove the plot frame lines. They are unnecessary chartjunk.
ax = plt.subplot(111)
ax.spines["top"].set_visible(False)
ax.spines["bottom"].set_visible(False)
ax.spines["right"].set_visible(False)
ax.spines["left"].set_visible(False)

# Ensure that the axis ticks only show up on the bottom and left of the plot.
# Ticks on the right and top of the plot are generally unnecessary chartjunk.
ax.get_xaxis().tick_bottom()
ax.get_yaxis().tick_left()

# Limit the range of the plot to only where the data is.
# Avoid unnecessary whitespace.
plt.ylim(895, 896.8)
plt.xlim(-2, 240)

# Make sure your axis ticks are large enough to be easily read.
# You don't want your viewers squinting to read your plot.
plt.yticks()
plt.xticks()


plt.plot(data.time.values, data.p.values, lw=2.5, color=tableau20[0]);

plt.grid(b=True, which='both', color='0.65',linestyle='-')

plt.title('Pressure-Time for Data Given')
plt.xlabel('t (s)')
plt.ylabel('p (hPa)')

# Finally, save the figure as a PNG.
# You can also save it as a PDF, JPEG, etc.
# Just change the file extension in this call.
# bbox_inches="tight" removes all the extra whitespace on the edges of your plot.
plt.savefig("pressuretime.png", bbox_inches="tight")

py_fig1 = tls.mpl_to_plotly(plt.gcf())
plotly.offline.plot(py_fig1, filename="pressure.html")

plt.clf();

data2 = pd.read_csv(
    "data.csv", usecols=[0, 1, 2, 3])

# Remove the plot frame lines. They are unnecessary chartjunk.
ax = plt.subplot(111)
ax.spines["top"].set_visible(False)
ax.spines["bottom"].set_visible(False)
ax.spines["right"].set_visible(False)
ax.spines["left"].set_visible(False)

# Ensure that the axis ticks only show up on the bottom and left of the plot.
# Ticks on the right and top of the plot are generally unnecessary chartjunk.
ax.get_xaxis().tick_bottom()
ax.get_yaxis().tick_left()

# Limit the range of the plot to only where the data is.
# Avoid unnecessary whitespace.
plt.ylim(-70, 70)
plt.xlim(-2, 240)

headers = ['a_x', 'a_y', 'a_z']

for rank, column in enumerate(headers):
    plt.plot(data2.time.values, data2[column].values, lw=2.5, color=tableau20[rank])
    plt.text(239.5, data2[column].values[rank]+rank*6, column, fontsize=14, color=tableau20[rank])

plt.grid(b=True, which='both', color='0.65',linestyle='-')

plt.title('Acceleration-Time for Data Given')
plt.xlabel('t (s)')
plt.ylabel('a (m/s^2)')

# Finally, save the figure as a PNG.
# You can also save it as a PDF, JPEG, etc.
# Just change the file extension in this call.
# bbox_inches="tight" removes all the extra whitespace on the edges of your plot.
plt.savefig("accelerationtime.png", bbox_inches="tight")

py_fig2 = tls.mpl_to_plotly(plt.gcf())
plotly.offline.plot(py_fig2, filename="accelerations.html")
